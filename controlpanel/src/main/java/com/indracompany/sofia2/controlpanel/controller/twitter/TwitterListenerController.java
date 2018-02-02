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
package com.indracompany.sofia2.controlpanel.controller.twitter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.indracompany.sofia2.config.model.Configuration;
import com.indracompany.sofia2.config.model.Ontology;
import com.indracompany.sofia2.config.model.TwitterListener;
import com.indracompany.sofia2.controlpanel.utils.AppWebUtils;
import com.indracompany.sofia2.service.ontology.OntologyService;
import com.indracompany.sofia2.service.twitter.TwitterService;

@Controller
@RequestMapping("/twitter")
public class TwitterListenerController {
	
	@Autowired
	AppWebUtils utils;
	@Autowired
	TwitterService twitterService;
	@Autowired
	OntologyService ontologyService;
	public static final String ROLE_ADMINISTRATOR="ROLE_ADMINISTRATOR";
	public static final String DATAMODEL_TWITTER="TWEET_DATAMODEL";
	
	@GetMapping("/scheduledsearch/list")
	public String list(Model model)
	{
		model.addAttribute("twitterListens", this.twitterService.getAllListensByUserId(utils.getUserId()));
		return "redirect:/main";
	}
	
	@GetMapping("/scheduledsearch/create")
	public String createForm(Model model)
	{
		this.loadOntologiesAndConfigurations(model);
		model.addAttribute("twitterListen",new TwitterListener());
		return "/twitter/scheduledsearch/create";
	}
	
	@GetMapping("/scheduledsearch/update/{id}")
	public String updateForm(Model model,@PathVariable ("id") String id)
	{
		TwitterListener twitterListener=null;
		twitterListener=this.twitterService.getListenById(id);
		if(twitterListener==null) twitterListener= new TwitterListener();
		model.addAttribute("twitterListen", twitterListener);
		this.loadOntologiesAndConfigurations(model);
		return "/twitter/scheduledsearch/create";
	}
	@PostMapping("/scheduledsearch/create")
	public String create(Model model,@ModelAttribute TwitterListener twitterListener)
	{
		if(twitterListener!=null)
		{
			this.twitterService.createListen(twitterListener);
		}
		return "redirec:/twitter/scheduledsearch/create";
	}
	
	
	@PostMapping("/scheduledsearch/getclients")
	public @ResponseBody List<String> getClientsOntology(@RequestBody String ontologyId)
	{
		return this.twitterService.getClientsFromOntology(ontologyId);
	}
	
	@PostMapping("/scheduledsearch/gettokens")
	public @ResponseBody List<String> getTokensClient(@RequestBody String clientPlatformId)
	{
		return this.twitterService.getTokensFromClient(clientPlatformId);
	}
	
	public void loadOntologiesAndConfigurations(Model model)
	{
		List<Configuration> configurations= new ArrayList<Configuration>();
		List<Ontology> ontologies=new ArrayList<Ontology>();
		if(this.utils.getRole().equals(ROLE_ADMINISTRATOR))
		{
			configurations=this.twitterService.getAllConfigurations();
			ontologies=this.ontologyService.getAllOntologies();
		}else
		{
			configurations=this.twitterService.getConfigurationsByUserId(this.utils.getUserId());
			for(Ontology ontology: this.ontologyService.getOntologiesByUserId(this.utils.getUserId()))
			{
				if(ontology.getDataModelId().getIdentification().equals(DATAMODEL_TWITTER))
				{
					ontologies.add(ontology);
				}
			}
			
		}
		
		model.addAttribute("configurations",configurations);
		model.addAttribute("ontologies",ontologies);
		
	}

}