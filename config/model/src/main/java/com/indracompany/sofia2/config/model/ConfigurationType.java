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
package com.indracompany.sofia2.config.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;

import com.indracompany.sofia2.config.model.base.AuditableEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CONFIGURATION_TYPE")
@Configurable
public class ConfigurationType extends AuditableEntity {

	public static enum Type {
		EndpointModulesConfiguration, TwitterConfiguration, MailConfiguration, RTDBConfiguration, MonitoringConfiguration, SchedulingConfiguration
	}

	@Id
	@Column(name = "ID", length = 50, unique = true, nullable = false)
	@NotNull
	@Getter
	@Setter
	private String id;

	public void setIdEnum(ConfigurationType.Type type) {
		this.id = type.toString();
	}

	@Column(name = "DESCRIPTION", length = 255)
	@Getter
	@Setter
	private String description;
}
