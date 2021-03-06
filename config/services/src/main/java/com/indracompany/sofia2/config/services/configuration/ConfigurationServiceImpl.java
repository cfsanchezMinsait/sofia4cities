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
package com.indracompany.sofia2.config.services.configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.indracompany.sofia2.config.components.AllConfiguration;
import com.indracompany.sofia2.config.components.TwitterConfiguration;
import com.indracompany.sofia2.config.model.Configuration;
import com.indracompany.sofia2.config.model.Configuration.Environment;
import com.indracompany.sofia2.config.model.ConfigurationType;
import com.indracompany.sofia2.config.model.User;
import com.indracompany.sofia2.config.repository.ConfigurationRepository;
import com.indracompany.sofia2.config.repository.ConfigurationTypeRepository;
import com.indracompany.sofia2.config.services.exceptions.ConfigServiceException;
import com.indracompany.sofia2.config.services.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConfigurationServiceImpl implements ConfigurationService {
	@Autowired
	private ConfigurationRepository configurationRepository;
	@Autowired
	private ConfigurationTypeRepository configurationTypeRepository;
	@Autowired
	private UserService userService;

	@Override
	public List<Configuration> getAllConfigurations() {
		return this.configurationRepository.findAll();
	}

	@Override
	@Transactional
	public void deleteConfiguration(String id) {
		this.configurationRepository.deleteById(id);
	}

	@Override
	public List<ConfigurationType> getAllConfigurationTypes() {
		List<ConfigurationType> types = this.configurationTypeRepository.findAll();
		return types;

	}

	@Override
	public Configuration getConfiguration(String id) {
		return this.configurationRepository.findById(id);
	}

	@Override
	public void createConfiguration(Configuration configuration) {
		Configuration oldConfiguration = this.configurationRepository.findById(configuration.getId());
		if (oldConfiguration != null)
			throw new ConfigServiceException(
					"You cann´t create a Configuration that exists:" + configuration.toString());
		oldConfiguration = this.configurationRepository.findByConfigurationTypeAndEnvironmentAndSuffix(
				configuration.getConfigurationType(), configuration.getEnvironment(), configuration.getSuffix());
		if (oldConfiguration != null)
			throw new ConfigServiceException(
					"Exist a configuration of this type for the environment and suffix:" + configuration.toString());

		oldConfiguration = new Configuration();
		oldConfiguration.setUser(configuration.getUser());
		oldConfiguration.setConfigurationType(configuration.getConfigurationType());
		oldConfiguration.setYmlConfig(configuration.getYmlConfig());
		oldConfiguration.setDescription(configuration.getDescription());
		oldConfiguration.setSuffix(configuration.getSuffix());
		oldConfiguration.setEnvironment(configuration.getEnvironment());
		this.configurationRepository.save(oldConfiguration);

	}

	@Override
	// FIXME: Check Exception
	public void updateConfiguration(Configuration configuration) {
		Configuration oldConfiguration = this.configurationRepository.findById(configuration.getId());
		if (oldConfiguration != null) {
			oldConfiguration.setYmlConfig(configuration.getYmlConfig());
			oldConfiguration.setDescription(configuration.getDescription());
			oldConfiguration.setSuffix(configuration.getSuffix());
			oldConfiguration.setEnvironment(configuration.getEnvironment());
			this.configurationRepository.save(oldConfiguration);

		} else {
			throw new ConfigServiceException("You cann´t update a Configuration:" + configuration.toString());
		}
	}

	@Override
	public TwitterConfiguration getTwitterConfiguration(String environment, String suffix) {
		try {
			Configuration config = this.getConfiguration(ConfigurationType.Type.TwitterConfiguration, environment,
					suffix);
			Constructor constructor = new Constructor(AllConfiguration.class);
			Yaml yaml = new Yaml(constructor);
			AllConfiguration tConfig = yaml.loadAs(config.getYmlConfig(), AllConfiguration.class);
			return tConfig.getTwitter();
		} catch (Exception e) {
			log.error("Error getting TwitterConfiguration", e);
			throw new ConfigServiceException("Error getting TwitterConfiguration", e);
		}
	}

	@Override
	public boolean existsConfiguration(Configuration configuration) {
		if (this.configurationRepository.findById(configuration.getId()) == null)
			return false;
		else
			return true;
	}

	@Override
	public Map fromYaml(String yaml) {
		Yaml yamlParser = new Yaml();
		return (Map) yamlParser.load(yaml);
	}

	@Override
	public boolean isValidYaml(final String yml) {
		try {
			Yaml yamlParser = new Yaml();
			yamlParser.load(yml);
			return true;
		} catch (Exception e) {
			log.error("Error parsing file:" + e.getMessage());
			return false;
		}
	}

	@Override
	public List<Configuration> getConfigurations(ConfigurationType.Type configurationTypeId) {
		ConfigurationType confType = this.configurationTypeRepository.findById(configurationTypeId.toString());
		return this.configurationRepository.findByConfigurationType(confType);
	}
	
	@Override
	public List<Configuration> getConfigurations(ConfigurationType.Type configurationTypeId, User user){
		ConfigurationType confType = this.configurationTypeRepository.findById(configurationTypeId.toString());
		return this.configurationRepository.findByConfigurationTypeAndUser(confType,user);
	}
	@Override
	public Configuration getConfiguration(ConfigurationType.Type configurationTypeId, String environment,
			String suffix) {
		ConfigurationType confType = this.configurationTypeRepository.findById(configurationTypeId.toString());
		return this.configurationRepository.findByConfigurationTypeAndEnvironmentAndSuffix(confType, environment,
				suffix);
	}
	
	@Override
	public Configuration getConfigurationByDescription(String description)
	{
		return this.configurationRepository.findByDescription(description);
	}

	@Override
	public List<Environment> getEnvironmentValues() {
		return Arrays.asList(Configuration.Environment.values());
	}

}
