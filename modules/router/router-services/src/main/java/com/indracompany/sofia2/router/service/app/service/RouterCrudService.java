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
package com.indracompany.sofia2.router.service.app.service;

import com.indracompany.sofia2.router.service.app.model.OperationModel;
import com.indracompany.sofia2.router.service.app.model.OperationResultModel;

public interface RouterCrudService {
	
	public OperationResultModel insert (OperationModel operationModel);
	public OperationResultModel update (OperationModel model);
	public OperationResultModel delete (OperationModel model);
	public OperationResultModel query (OperationModel model);
	public OperationResultModel execute (OperationModel model);
		
}


