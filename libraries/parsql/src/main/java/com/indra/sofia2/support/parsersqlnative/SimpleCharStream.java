/**
 * Copyright Indra Sistemas, S.A.
 * 2013-2018 SPAIN
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*******************************************************************************
 * © Indra Sistemas, S.A.
 * 2013 - 2014  SPAIN
 * 
 * All rights reserved
 ******************************************************************************/
package com.indra.sofia2.support.parsersqlnative;

public class SimpleCharStream
{
	public static final boolean STATIC = false;
	int bufsize;
	int available;
	int tokenBegin;
	public int bufpos = -1;
	protected int bufline[];
	protected int bufcolumn[];

	protected int column = 0;
	protected int line = 1;

	protected boolean prevCharIsCR = false;
	protected boolean prevCharIsLF = false;

	protected java.io.Reader inputStream;
	protected String sqlStream;

	protected char[] buffer;
	protected int maxNextCharInd = 0;
	protected int inBuf = 0;
	protected int tabSize = 8;

	public SimpleCharStream(String dstream, String encoding, int startline, int startcolumn, int buffersize) throws java.io.UnsupportedEncodingException{
		this(dstream, startline, startcolumn, buffersize);
		
	}
	public SimpleCharStream(java.io.InputStream dstream, String encoding, int startline,
			int startcolumn, int buffersize) throws java.io.UnsupportedEncodingException
			{
		this(encoding == null ? new java.io.InputStreamReader(dstream) : new java.io.InputStreamReader(dstream, encoding), startline, startcolumn, buffersize);
			}

	public SimpleCharStream(java.io.InputStream dstream, int startline,
			int startcolumn, int buffersize)
	{
		this(new java.io.InputStreamReader(dstream), startline, startcolumn, buffersize);
	}

	public SimpleCharStream (String dstream, String encoding, int startline, int startcolumn) throws java.io.UnsupportedEncodingException{
		this(dstream, encoding, startline, startcolumn, 4096);
	}
	public SimpleCharStream(java.io.InputStream dstream, String encoding, int startline,
			int startcolumn) throws java.io.UnsupportedEncodingException
			{
		this(dstream, encoding, startline, startcolumn, 4096);
			}

	public SimpleCharStream(java.io.InputStream dstream, int startline,
			int startcolumn)
	{
		this(dstream, startline, startcolumn, 4096);
	}

	public SimpleCharStream(java.io.InputStream dstream, String encoding) throws java.io.UnsupportedEncodingException
	{
		this(dstream, encoding, 1, 1, 4096);
	}

	public SimpleCharStream(java.io.InputStream dstream)
	{
		this(dstream, 1, 1, 4096);
	}
	
	public SimpleCharStream(String dstream, int startline, int startcolumn, int buffersize){
		sqlStream = dstream;
		line = startline;
		column = startcolumn - 1;

		available = bufsize = buffersize;
		buffer = new char[buffersize];
		bufline = new int[buffersize];
		bufcolumn = new int[buffersize];
	}
	
	public SimpleCharStream(java.io.Reader dstream, int startline, int startcolumn, int buffersize){
		inputStream = dstream;
		line = startline;
		column = startcolumn - 1;

		available = bufsize = buffersize;
		buffer = new char[buffersize];
		bufline = new int[buffersize];
		bufcolumn = new int[buffersize];
	}

	public SimpleCharStream(java.io.Reader dstream, int startline,
			int startcolumn){
		this(dstream, startline, startcolumn, 4096);
	}

	public SimpleCharStream(java.io.Reader dstream){
		this(dstream, 1, 1, 4096);
	}

	protected void setTabSize(int i) {
		tabSize = i; }

	protected int getTabSize(int i) { 
		return tabSize; }

	protected void expandBuff(boolean wrapAround){

		char[] newbuffer = new char[bufsize + 2048];
		int newbufline[] = new int[bufsize + 2048];
		int newbufcolumn[] = new int[bufsize + 2048];

		try{

			if (wrapAround)
			{
				System.arraycopy(buffer, tokenBegin, newbuffer, 0, bufsize - tokenBegin);
				System.arraycopy(buffer, 0, newbuffer, bufsize - tokenBegin, bufpos);
				buffer = newbuffer;

				System.arraycopy(bufline, tokenBegin, newbufline, 0, bufsize - tokenBegin);
				System.arraycopy(bufline, 0, newbufline, bufsize - tokenBegin, bufpos);
				bufline = newbufline;

				System.arraycopy(bufcolumn, tokenBegin, newbufcolumn, 0, bufsize - tokenBegin);
				System.arraycopy(bufcolumn, 0, newbufcolumn, bufsize - tokenBegin, bufpos);
				bufcolumn = newbufcolumn;

				maxNextCharInd = (bufpos += (bufsize - tokenBegin));
			}
			else
			{
				System.arraycopy(buffer, tokenBegin, newbuffer, 0, bufsize - tokenBegin);
				buffer = newbuffer;

				System.arraycopy(bufline, tokenBegin, newbufline, 0, bufsize - tokenBegin);
				bufline = newbufline;

				System.arraycopy(bufcolumn, tokenBegin, newbufcolumn, 0, bufsize - tokenBegin);
				bufcolumn = newbufcolumn;

				maxNextCharInd = (bufpos -= tokenBegin);
			}
		}
		catch (Throwable t){
			throw new Error(t.getMessage());
		}


		bufsize += 2048;
		available = bufsize;
		tokenBegin = 0;
	}

	protected void fillBuff() throws java.io.IOException
	{
		if (maxNextCharInd == available){
			if (available == bufsize){
				if (tokenBegin > 2048){
					bufpos = maxNextCharInd = 0;
					available = tokenBegin;
				} else if (tokenBegin < 0) {
					bufpos = maxNextCharInd = 0;
				} else {
					expandBuff(false);
				}
			} else if (available > tokenBegin) {
				available = bufsize;
			} else if ((tokenBegin - available) < 2048) {
				expandBuff(true);
			} else {
				available = tokenBegin;
			}
		}

		int i;

		try {
			if ((i = inputStream.read(buffer, maxNextCharInd, available - maxNextCharInd)) == -1)
			{
				inputStream.close();
				throw new java.io.IOException();
			} else {
				maxNextCharInd += i;
			}
			return;
		}
		catch(java.io.IOException e) {
			--bufpos;
			backup(0);
			if (tokenBegin == -1) {
				tokenBegin = bufpos;
			}
			throw e;
		}
	}

	public char beginToken() throws java.io.IOException
	{
		tokenBegin = -1;
		char c = readChar();
		tokenBegin = bufpos;

		return c;
	}

	protected void updateLineColumn(char c)
	{
		column++;

		if (prevCharIsLF)
		{
			prevCharIsLF = false;
			line += (column = 1);
		}
		else if (prevCharIsCR)
		{
			prevCharIsCR = false;
			if (c == '\n')
			{
				prevCharIsLF = true;
			}
			else
				line += (column = 1);
		}

		switch (c)
		{
		case '\r' :
			prevCharIsCR = true;
			break;
		case '\n' :
			prevCharIsLF = true;
			break;
		case '\t' :
			column--;
			column += (tabSize - (column % tabSize));
			break;
		default :
			break;
		}

		bufline[bufpos] = line;
		bufcolumn[bufpos] = column;
	}

	public char readChar() throws java.io.IOException
	{
		if (inBuf > 0)
		{
			--inBuf;

			if (++bufpos == bufsize)
				bufpos = 0;

			return buffer[bufpos];
		}

		if (++bufpos >= maxNextCharInd)
			fillBuff();

		char c = buffer[bufpos];

		updateLineColumn(c);
		return (c);
	}

	public int getEndColumn() {
		return bufcolumn[bufpos];
	}

	public int getEndLine() {
		return bufline[bufpos];
	}

	public int getBeginColumn() {
		return bufcolumn[tokenBegin];
	}

	public int getBeginLine() {
		return bufline[tokenBegin];
	}

	public void backup(int amount) {

		inBuf += amount;
		if ((bufpos -= amount) < 0)
			bufpos += bufsize;
	}

	public void reInit(java.io.Reader dstream, int startline,
			int startcolumn, int buffersize){
		inputStream = dstream;
		line = startline;
		column = startcolumn - 1;

		if (buffer == null || buffersize != buffer.length)
		{
			available = bufsize = buffersize;
			buffer = new char[buffersize];
			bufline = new int[buffersize];
			bufcolumn = new int[buffersize];
		}
		prevCharIsLF = prevCharIsCR = false;
		tokenBegin = inBuf = maxNextCharInd = 0;
		bufpos = -1;
	}

	public void reInit(java.io.Reader dstream, int startline,
			int startcolumn){
		reInit(dstream, startline, startcolumn, 4096);
	}

	public void reInit(java.io.Reader dstream)
	{
		reInit(dstream, 1, 1, 4096);
	}

	public void reInit(java.io.InputStream dstream, String encoding, int startline,
			int startcolumn, int buffersize) throws java.io.UnsupportedEncodingException
			{
		reInit(encoding == null ? new java.io.InputStreamReader(dstream) : new java.io.InputStreamReader(dstream, encoding), startline, startcolumn, buffersize);
			}

	public void reInit(java.io.InputStream dstream, int startline,
			int startcolumn, int buffersize)
	{
		reInit(new java.io.InputStreamReader(dstream), startline, startcolumn, buffersize);
	}

	public void reInit(java.io.InputStream dstream, String encoding) throws java.io.UnsupportedEncodingException
	{
		reInit(dstream, encoding, 1, 1, 4096);
	}

	public void reInit(java.io.InputStream dstream)
	{
		reInit(dstream, 1, 1, 4096);
	}
	public void reInit(java.io.InputStream dstream, String encoding, int startline,
			int startcolumn) throws java.io.UnsupportedEncodingException
			{
		reInit(dstream, encoding, startline, startcolumn, 4096);
			}

	public void reInit(java.io.InputStream dstream, int startline,
			int startcolumn)
	{
		reInit(dstream, startline, startcolumn, 4096);
	}

	public String getImage()
	{
		if (bufpos >= tokenBegin)
			return new String(buffer, tokenBegin, bufpos - tokenBegin + 1);
		else
			return new String(buffer, tokenBegin, bufsize - tokenBegin) +
					new String(buffer, 0, bufpos + 1);
	}

	public char[] getSuffix(int len)
	{
		char[] ret = new char[len];

		if ((bufpos + 1) >= len) {
			System.arraycopy(buffer, bufpos - len + 1, ret, 0, len);
		} else {
			System.arraycopy(buffer, bufsize - (len - bufpos - 1), ret, 0,
					len - bufpos - 1);
			System.arraycopy(buffer, 0, ret, len - bufpos - 1, bufpos + 1);
		}

		return ret;
	}

	public void done()
	{
		buffer = null;
		bufline = null;
		bufcolumn = null;
	}

	public void adjustBeginLineColumn(int newLine, int newCol)
	{
		int newLineAux = newLine;
		int start = tokenBegin;
		int len;

		if (bufpos >= tokenBegin)
		{
			len = bufpos - tokenBegin + inBuf + 1;
		}
		else
		{
			len = bufsize - tokenBegin + bufpos + 1 + inBuf;
		}

		int i = 0, j = 0, k = 0;
		int nextColDiff = 0, columnDiff = 0;

		while (i < len &&
				bufline[j = start % bufsize] == bufline[k = ++start % bufsize])
		{
			bufline[j] = newLineAux;
			nextColDiff = columnDiff + bufcolumn[k] - bufcolumn[j];
			bufcolumn[j] = newCol + columnDiff;
			columnDiff = nextColDiff;
			i++;
		} 

		if (i < len)
		{
			bufline[j] = newLineAux++;
			bufcolumn[j] = newCol + columnDiff;

			while (i++ < len)
			{
				if (bufline[j = start % bufsize] != bufline[++start % bufsize]) {
					bufline[j] = newLineAux++;
				} else {
					bufline[j] = newLineAux;
				}
			}
		}

		line = bufline[j];
		column = bufcolumn[j];
	}

}
