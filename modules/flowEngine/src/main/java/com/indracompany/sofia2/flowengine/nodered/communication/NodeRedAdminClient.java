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
package com.indracompany.sofia2.flowengine.nodered.communication;

import java.util.List;

import com.indracompany.sofia2.flowengine.nodered.communication.dto.FlowEngineDomain;
import com.indracompany.sofia2.flowengine.nodered.communication.dto.FlowEngineDomainStatus;

public interface NodeRedAdminClient {

	// Shuts down the Flow Engine Admin
	public String stopFlowEngine();

	// Flow Engine Domain Management
	public void stopFlowEngineDomain(String domain);

	public String startFlowEngineDomain(FlowEngineDomain domain);

	public String createFlowengineDomain(FlowEngineDomain domain);

	public void deleteFlowEngineDomain(String domainId);

	public FlowEngineDomain getFlowEngineDomain(String domainId);

	// Generic Flow Engine Requests
	public List<FlowEngineDomainStatus> getAllFlowEnginesDomains();

	public List<FlowEngineDomainStatus> getFlowEngineDomainStatus(List<String> domainList);

	// Synchronization of the active/inactive domains with CDB
	void resetSynchronizedWithBDC();

	public String synchronizeMF(List<FlowEngineDomainStatus> domainList);

}
