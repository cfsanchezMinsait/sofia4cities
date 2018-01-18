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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;


import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Configurable;

import com.indracompany.sofia2.config.model.base.AuditableEntity;

import lombok.Getter;
import lombok.Setter;

@Configurable
@Entity
@Table(name = "CONSOLE_MENU_OPTION")
public class ConsoleMenuOption extends AuditableEntity{

	@Id
	@Column(name = "ID", length = 50)
	@Getter @Setter private String id;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "CONSOLE_MENU_ID", referencedColumnName = "id", nullable = false)
	@Getter @Setter private ConsoleMenu consoleMenuId;

	@Column(name = "OPTION_MENU", length = 75,nullable = false)
	@NotNull
	@Getter @Setter private String option;

	@Column(name = "PERMITTED")
	@NotNull
	@Getter @Setter private boolean permitted;



}
