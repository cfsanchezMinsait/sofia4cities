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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Configurable;

import com.indracompany.sofia2.config.model.base.AuditableEntityWithUUID;

@Configurable
@Entity
@Table(name = "Api_Authentication_Attribute")
@SuppressWarnings("deprecation")
public class ApiAuthenticationAttribute extends AuditableEntityWithUUID {

    private static final long serialVersionUID = 1L;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
   
    @JoinColumn(name = "autparamId", referencedColumnName = "ID", nullable = false)
    private ApiAuthenticationParameter autparamId;

	@Column(name = "NAME", length = 50, nullable = false)
    @NotNull
    private String name;

	@Column(name = "VALUE", length = 512,nullable = false)
    @NotNull
    private String value;

	public ApiAuthenticationParameter getAutparamId() {
		return autparamId;
	}

	public void setAutparamId(ApiAuthenticationParameter autparamId) {
		this.autparamId = autparamId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}