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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Configurable;

import com.indracompany.sofia2.config.model.base.AuditableEntityWithUUID;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CONFIGURATION")
@Configurable
public class Configuration extends AuditableEntityWithUUID {

	public static enum Environment {
		ALL, LOCAL, DEV, PRE, PRO, OTHER
	}

	@ManyToOne
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", nullable = false)
	@Getter
	@Setter
	private User user;

	@Column(name = "YML_CONFIG", nullable = false)
	@NotNull
	@Lob
	@Getter
	@Setter
	private String ymlConfig;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	@JoinColumn(name = "CONFIGURATION_TYPE", referencedColumnName = "ID", nullable = false)
	@Getter
	@Setter
	private ConfigurationType configurationType;

	@Column(name = "environment", length = 50)
	@Getter
	@Setter
	private String environment;

	public void setEnvironmentEnum(Configuration.Environment env) {
		this.environment = env.toString();
	}

	@Column(name = "SUFFIX", length = 50)
	@Getter
	@Setter
	private String suffix;

	@Column(name = "DESCRIPTION", length = 255)
	@Getter
	@Setter
	private String description;

}
