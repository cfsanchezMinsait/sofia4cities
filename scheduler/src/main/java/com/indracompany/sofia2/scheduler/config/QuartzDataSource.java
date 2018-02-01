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
package com.indracompany.sofia2.scheduler.config;


import static com.indracompany.sofia2.scheduler.PropertyNames.SCHEDULER_PREFIX;
import static com.indracompany.sofia2.scheduler.PropertyNames.SCHEDULER_PROPERTIES_LOCATION;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ConditionalOnResource(resources = SCHEDULER_PROPERTIES_LOCATION)
@EnableTransactionManagement
@EnableJpaRepositories(
  entityManagerFactoryRef = "quartzEntityManagerFactory",
  transactionManagerRef = "quartzTransactionManager",
  basePackages = { "com.indracompany.sofia2.scheduler" }
)
public class QuartzDataSource {
	
	
	@Bean
	@ConfigurationProperties("quartz.jpa")
	public JpaProperties quartzJpaProperties() {
	    return new JpaProperties();
	}
	
	@Bean("quartzDatasource")
	@ConfigurationProperties("quartz.datasource")
	public DataSource quartzDatasource() {
	    return  DataSourceBuilder.create().build();
	}
	
	@Bean("quartzEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean quartzEntityManagerFactory(EntityManagerFactoryBuilder builder,
																	   @Qualifier("quartzDatasource") DataSource dataSource) {
			
		Map<String,String> prop = quartzJpaProperties().getProperties();
		
		return builder
		      .dataSource(dataSource)
		      .packages("com.indracompany.sofia2.scheduler")
		      .persistenceUnit("quartz").properties(quartzJpaProperties().getProperties())
		      .build();
	}
	
	@Bean(name = "quartzTransactionManager")
	public PlatformTransactionManager quartzTransactionManager(@Qualifier("quartzEntityManagerFactory") 
														 EntityManagerFactory  entityManagerFactory) {
	    return new JpaTransactionManager(entityManagerFactory);
	}

}
