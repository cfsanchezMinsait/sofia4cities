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
package com.indracompany.sofia2.api.util;
import java.util.Collections;
import java.util.Enumeration;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class RequestDumpUtil {

    private static final String INDENT = "\t";
    private static final String LF = "\n";
    
    public static String getValueFromRequest(String key,HttpServletRequest request ) {
    	
    	String value = null;
    	value = request.getParameter(key);
    	if (value==null)
    		value = request.getParameter("$"+key);
    	if (value==null)
    		value = request.getHeader(key);
    	if (value==null)
    		value = request.getHeader("$"+key);
    	
    	return value;
    	
    }
    
 public static String getContentType(HttpServletRequest request ) {
    	
	 return request.getContentType();
    	
    }

    public static void dumpRequest(StringBuilder sb, HttpServletRequest request) {
        sb.append(INDENT).append("[Class] ").append(request.getClass().getName()).append(", instance=" + request.toString().trim()).append(LF);
        sb.append(INDENT).append("[RequestedSessionId] ").append(request.getRequestedSessionId()).append(LF);
        sb.append(INDENT).append("[RequestURI] ").append(request.getRequestURI()).append(LF);
        sb.append(INDENT).append("[ServletPath] ").append(request.getServletPath()).append(LF);
        sb.append(INDENT).append("[CharacterEncoding] ").append(request.getCharacterEncoding()).append(LF);
        sb.append(INDENT).append("[ContentLength] ").append(request.getContentLength()).append(LF);
        sb.append(INDENT).append("[ContentType] ").append(request.getContentType()).append(LF);
        sb.append(INDENT).append("[Locale] ").append(request.getLocale()).append(LF);
        sb.append(INDENT).append("[Locales] ").append(Collections.list(request.getLocales()).stream().map(loc -> loc.toString()).collect(Collectors.joining(", "))).append(LF);
        sb.append(INDENT).append("[Scheme] ").append(request.getScheme()).append(LF);
        sb.append(INDENT).append("[isSecure] ").append(request.isSecure()).append(LF);
        sb.append(INDENT).append("[Protocol] ").append(request.getProtocol()).append(LF);
        sb.append(INDENT).append("[RemoteAddr] ").append(request.getRemoteAddr()).append(LF);
        sb.append(INDENT).append("[RemoteHost] ").append(request.getRemoteHost()).append(LF);
        sb.append(INDENT).append("[ServerName] ").append(request.getServerName()).append(LF);
        sb.append(INDENT).append("[ServerPort] ").append(request.getServerPort()).append(LF);
        sb.append(INDENT).append("[ContextPath] ").append(request.getContextPath()).append(LF);
        sb.append(INDENT).append("[Method] ").append(request.getMethod()).append(LF);
        sb.append(INDENT).append("[QueryString] ").append(request.getQueryString()).append(LF);
        sb.append(INDENT).append("[PathInfo] ").append(request.getPathInfo()).append(LF);
        sb.append(INDENT).append("[RemoteUser] ").append(request.getRemoteUser()).append(LF);
    }

    public static void dumpRequestHeader(StringBuilder sb, HttpServletRequest request) {
        Enumeration<String> hNames = request.getHeaderNames();
        while (hNames.hasMoreElements()) {
            String name = hNames.nextElement();
            sb.append(INDENT).append("[header] ").append(name).append("=").append(request.getHeader(name)).append(LF);
        }
    }

    public static void dumpRequestParameter(StringBuilder sb, HttpServletRequest request) {
        Enumeration<String> pNames = request.getParameterNames();
        while (pNames.hasMoreElements()) {
            String name = pNames.nextElement();
            sb.append(INDENT).append("[param] ").append(name).append("=").append(request.getParameter(name)).append(LF);
        }
    }

    public static void dumpRequestSessionAttribute(StringBuilder sb, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            Enumeration<String> aNames = session.getAttributeNames();
            while (aNames.hasMoreElements()) {
                String name = aNames.nextElement();
                sb.append(INDENT).append("[session] ").append(name).append("=").append(session.getAttribute(name)).append(LF);
            }
        }
    }

}