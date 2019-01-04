package com.sant.hms.ui.controllers.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.sant.hms.ui.controllers.GreetingController;
import com.sant.hms.ui.pojo.Message;

@RestController
public class GreetingControllerImpl implements GreetingController {

	private static final Logger log = LoggerFactory.getLogger(GreetingControllerImpl.class);
	
	@Autowired
	private RestTemplate restTemplate;	
    @Autowired
    private EurekaClient eurekaClient;
    
	
	@Override
	public Message getMessage() {
		
		InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("gateway",false);
		
		String path = instanceInfo.getHomePageUrl() + "resource";
		
		Message message = restTemplate.getForObject(path, Message.class);
		
		return message;
	}

	
	
}
