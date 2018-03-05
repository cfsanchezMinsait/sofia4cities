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
package com.indracompany.sofia2.router.client;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommand.Setter;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;


@Component
public class RouterClientGateway<T> {

	private  HystrixCommand.Setter config;
	private  HystrixCommandProperties.Setter commandProperties;
	private  HystrixThreadPoolProperties.Setter threadPoolProperties;
	
	private RouterClientCommand<T> routerClientCommand;

	@Value("${remoteservice.command.execution.timeout:10000}")
	private  int executionTimeout;

	@Value("${remoteservice.command.sleepwindow:5000}")
	private  int sleepWindow;

	@Value("${remoteservice.command.threadpool.maxsize:10}")
	private  int maxThreadCount;

	@Value("${remoteservice.command.threadpool.coresize:5}")
	private  int coreThreadCount;

	@Value("${remoteservice.command.task.queue.size:5}")
	private  int queueCount;

	@Value("${remoteservice.command.group.key:RouterClientGroup}")
	private  String groupKey;

	@Value("${remoteservice.command.key:RouterClientKey}")
	private  String key;

	@PostConstruct
	public void autoSetup() {
		this.config = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey));
		this.config = config.andCommandKey(HystrixCommandKey.Factory.asKey(key));

		this.commandProperties = HystrixCommandProperties.Setter();
		this.commandProperties.withExecutionTimeoutInMilliseconds(executionTimeout);
		this.commandProperties.withCircuitBreakerSleepWindowInMilliseconds(sleepWindow);

		this.threadPoolProperties = HystrixThreadPoolProperties.Setter();
		this.threadPoolProperties.withMaxQueueSize(maxThreadCount).withCoreSize(coreThreadCount)
				.withMaxQueueSize(queueCount);

		this.config.andCommandPropertiesDefaults(commandProperties);
		this.config.andThreadPoolPropertiesDefaults(threadPoolProperties);
	}
	
	
	public static Setter setup(String groupKey, String key, int executionTimeout, int sleepWindow, int coreThreadCount, int maxThreadCount, int queueCount ) {
		HystrixCommand.Setter config;
		HystrixCommandProperties.Setter commandProperties;
		HystrixThreadPoolProperties.Setter threadPoolProperties;
		
		config = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey));
		config = config.andCommandKey(HystrixCommandKey.Factory.asKey(key));

		commandProperties = HystrixCommandProperties.Setter();
		commandProperties.withExecutionTimeoutInMilliseconds(executionTimeout);
		commandProperties.withCircuitBreakerSleepWindowInMilliseconds(sleepWindow);

		threadPoolProperties = HystrixThreadPoolProperties.Setter();
		threadPoolProperties.withMaxQueueSize(maxThreadCount).withCoreSize(coreThreadCount)
				.withMaxQueueSize(queueCount);

		config.andCommandPropertiesDefaults(commandProperties);
		config.andThreadPoolPropertiesDefaults(threadPoolProperties);
		
		return config;
	}
	
	public static Setter setupDefault() {
		HystrixCommand.Setter config;
		HystrixCommandProperties.Setter commandProperties;
		HystrixThreadPoolProperties.Setter threadPoolProperties;
		
		config = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RouterClientGroup"));
		config = config.andCommandKey(HystrixCommandKey.Factory.asKey("RouterClientKey"));

		commandProperties = HystrixCommandProperties.Setter();
		commandProperties.withExecutionTimeoutInMilliseconds(10000);
		commandProperties.withCircuitBreakerSleepWindowInMilliseconds(5000);

		threadPoolProperties = HystrixThreadPoolProperties.Setter();
		threadPoolProperties.withMaxQueueSize(10).withCoreSize(5)
				.withMaxQueueSize(5);

		config.andCommandPropertiesDefaults(commandProperties);
		config.andThreadPoolPropertiesDefaults(threadPoolProperties);
		
		return config;
	}
	
	public RouterClientGateway (String name,  Setter config, RouterClient<T> routerClient) {
		this.config = config;
		this.routerClientCommand= new RouterClientCommand<T>(name,config,routerClient);
	}
	
	public RouterClientGateway (String name, RouterClient<T> routerClient) {
		this.routerClientCommand= new RouterClientCommand<T>(name,config,routerClient);
	}
	
	public T execute(T input) {
		routerClientCommand.setNotification(input);
		return routerClientCommand.execute();
	}
	
	public void setFallback(T input) {
		routerClientCommand.setFallback(input);
		
	}
	
	

}