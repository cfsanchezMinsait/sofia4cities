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
 * 2013 - 2018  SPAIN
 *
 * All rights reserved
 ******************************************************************************/

package com.indracompany.sofia2.config.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Configurable;

import com.indracompany.sofia2.config.model.base.AuditableEntityWithUUID;

import lombok.Getter;
import lombok.Setter;

@Configurable
@Entity
@Table(name = "DASHBOARD_PAGE")

public class DashboardPage extends AuditableEntityWithUUID {

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "DASHBOARD", referencedColumnName = "ID")
	@Getter @Setter private Dashboard dashboard;
	@Column(name = "IDENTIFICATION", length = 100,nullable = false)
    @NotNull
    @Getter @Setter private String 	identification;
	
	@Column(name = "DESCRIPTION", length = 100,nullable = false)
    @NotNull
    @Getter @Setter private String description;
	
	@Column(name = "MODEL")
    @NotNull
    @Type(type ="org.hibernate.type.TextType")
    @Getter @Setter private String model;
	
	@Column (name = "POSITION", length=10, nullable= false)
	@NotNull
	@Getter @Setter private Integer position;
	
	}
