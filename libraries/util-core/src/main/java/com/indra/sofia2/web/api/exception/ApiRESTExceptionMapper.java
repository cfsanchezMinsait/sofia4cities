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
package com.indra.sofia2.web.api.exception;

import javax.naming.AuthenticationException;
import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Component;

@Component("apiRESTExceptionMapper")
public class ApiRESTExceptionMapper implements ExceptionMapper<Throwable> {
	
	private static final String APPLICATIONJSON = "application/json";

	@Override
	public Response toResponse(Throwable exception) {
		if (exception instanceof AuthorizationServiceException) {
			return Response.status(Status.UNAUTHORIZED).type(APPLICATIONJSON).entity(exception.getMessage()).build();
		} else if (exception instanceof IllegalArgumentException) {
			return Response.status(Status.BAD_REQUEST).type(APPLICATIONJSON).entity(exception.getMessage()).build();
		} else if (exception instanceof PersistenceException) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(APPLICATIONJSON).entity(exception.getMessage()).build();
		} else if (exception instanceof AuthenticationException) {
			return Response.status(Status.BAD_REQUEST).type(APPLICATIONJSON).entity(exception.getMessage()).build();
		} else {
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(APPLICATIONJSON).entity(exception.getMessage()).build();
		}
	}

}
