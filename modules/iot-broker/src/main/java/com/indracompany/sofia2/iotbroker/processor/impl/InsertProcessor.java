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
package com.indracompany.sofia2.iotbroker.processor.impl;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.indracompany.sofia2.common.exception.AuthorizationException;
import com.indracompany.sofia2.common.exception.BaseException;
import com.indracompany.sofia2.iotbroker.common.exception.OntologySchemaException;
import com.indracompany.sofia2.iotbroker.common.exception.SSAPProcessorException;
import com.indracompany.sofia2.iotbroker.processor.MessageTypeProcessor;
import com.indracompany.sofia2.persistence.ContextData;
import com.indracompany.sofia2.persistence.interfaces.BasicOpsDBRepository;
import com.indracompany.sofia2.plugin.iotbroker.security.SecurityPluginManager;
import com.indracompany.sofia2.ssap.SSAPMessage;
import com.indracompany.sofia2.ssap.body.SSAPBodyInsertMessage;
import com.indracompany.sofia2.ssap.body.SSAPBodyReturnMessage;
import com.indracompany.sofia2.ssap.body.parent.SSAPBodyMessage;
import com.indracompany.sofia2.ssap.enums.SSAPMessageDirection;
import com.indracompany.sofia2.ssap.enums.SSAPMessageTypes;

@Component
public class InsertProcessor implements MessageTypeProcessor {

	@Autowired
	BasicOpsDBRepository repository;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	SecurityPluginManager securityPluginManager;
	//	@Autowired
	//	List<DBStatementParser> dbStatementParsers;

	@Override
	public SSAPMessage<SSAPBodyReturnMessage> process(SSAPMessage<? extends SSAPBodyMessage> message)
			throws BaseException {
		@SuppressWarnings("unchecked")
		final
		SSAPMessage<SSAPBodyInsertMessage> insertMessage = (SSAPMessage<SSAPBodyInsertMessage>) message;
		final SSAPMessage<SSAPBodyReturnMessage> responseMessage = new SSAPMessage<>();

		// TODO: Client Connection in contextData
		final ContextData contextData = new ContextData();

		contextData.setClientConnection("");
		contextData.setClientPatform(insertMessage.getBody().getClientPlatform());
		contextData.setClientPatformInstance(insertMessage.getBody().getClientPlatformInstance());
		contextData.setClientSession(insertMessage.getSessionKey());
		contextData.setTimezoneId(ZoneId.systemDefault().toString());
		contextData.setUser(securityPluginManager.getUserIdFromSessionKey(insertMessage.getSessionKey()));

		((ObjectNode) insertMessage.getBody().getData()).set("contextData", objectMapper.valueToTree(contextData));

		final String repositoryResponse = repository.insert(insertMessage.getOntology(),
				insertMessage.getBody().getData().toString());

		// TODO: SSAP Copy methods
		responseMessage.setDirection(SSAPMessageDirection.RESPONSE);
		responseMessage.setMessageId(insertMessage.getMessageId());
		responseMessage.setMessageType(insertMessage.getMessageType());
		responseMessage.setOntology(insertMessage.getOntology());
		responseMessage.setSessionKey(insertMessage.getSessionKey());
		responseMessage.setBody(new SSAPBodyReturnMessage());
		responseMessage.getBody().setOk(true);
		responseMessage.getBody().setClientPlatform(insertMessage.getBody().getClientPlatform());
		responseMessage.getBody().setClientPlatformInstance(insertMessage.getBody().getClientPlatformInstance());

		try {
			responseMessage.getBody().setData(objectMapper.readTree(repositoryResponse));
		} catch (final IOException e) {
			// TODO: LOG
			throw new SSAPProcessorException("Response from repository on insert is not JSON compliant");
		}

		return responseMessage;
	}

	@Override
	public List<SSAPMessageTypes> getMessageTypes() {
		return Collections.singletonList(SSAPMessageTypes.INSERT);
	}

	@Override
	public void validateMessage(SSAPMessage<? extends SSAPBodyMessage> message)
			throws AuthorizationException, OntologySchemaException, SSAPProcessorException {
		final SSAPMessage<SSAPBodyInsertMessage> operationMessage = (SSAPMessage<SSAPBodyInsertMessage>) message;

		//		if (operationMessage.getBody().getQueryType() == null) {
		//			throw new SSAPProcessorException(String.format(MessageException.ERR_QUERY_TYPE_MANDATORY,
		//					operationMessage.getBody().getQueryType()));
		//		}
		//
		//		final List<String> collections = this.getOntologies(message.getMessageType(),
		//				operationMessage.getBody().getQueryType(), operationMessage.getBody().getQuery());
		//
		//		if (!collections.contains(operationMessage.getOntology())) {
		//			collections.add(operationMessage.getOntology());
		//		}

		securityPluginManager.checkAuthorization(message.getMessageType(), operationMessage.getOntology(), message.getSessionKey());


		// TODO: Validate ontology Schema. The schema is stored in BDC or cache
		// validateOntologySchema("", message.getBody().getData().toString());
	}

	//	public void validateOntologySchema(String ontologySchema, String ontologyInstance) throws OntologySchemaException {
	//		try {
	//			final org.json.JSONObject jsonSchema = new org.json.JSONObject(new JSONTokener(ontologySchema));
	//
	//			final org.json.JSONObject jsonSubject = new org.json.JSONObject(new JSONTokener(ontologyInstance));
	//
	//			final Schema schema = SchemaLoader.load(jsonSchema);
	//			schema.validate(jsonSubject);
	//		} catch (final JSONException e) {
	//			// TODO: LOG
	//			throw new OntologySchemaException(String.format(MessageException.ERR_ONTOLOGY_SCHEMA, e.getMessage()));
	//		}
	//
	//	}

	//	private List<String> getOntologies(SSAPMessageTypes messageType, SSAPQueryType queryType, String query)
	//			throws AuthorizationException {
	//
	//		for (final DBStatementParser parser : dbStatementParsers) {
	//			if (queryType.equals(parser.getSSAPQueryTypeSupported())) {
	//				final Optional<AccessMode> accesType = SSAP2PersintenceUtil.formSSAPMessageType2TableAccesMode(messageType);
	//				final List<String> collections = parser.getCollectionList(query, accesType.get());
	//				return collections;
	//			}
	//		}
	//		return new ArrayList<>();
	//
	//	}

}
