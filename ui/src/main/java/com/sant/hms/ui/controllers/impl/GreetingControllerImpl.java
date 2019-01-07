package com.sant.hms.ui.controllers.impl;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.sant.hms.ui.controllers.GreetingController;
import com.sant.hms.ui.pojo.Message;
import com.sant.hms.ui.rabbitmq.RabbitMqConfig;
import com.sant.hms.ui.rabbitmq.Receiver;

@RestController
public class GreetingControllerImpl implements GreetingController {

	private static final Logger log = LoggerFactory.getLogger(GreetingControllerImpl.class);
	
	@Autowired
	private RestTemplate restTemplate;	
    @Autowired
    private EurekaClient eurekaClient;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Receiver receiver;
    
    @HystrixCommand(fallbackMethod = "fallbackMessage",
    	    commandProperties = {
    	            @HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE")
    	        })
	@Override
	public Message getMessage() {
		
		String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		
		Set<String> roles = AuthorityUtils.authorityListToSet( SecurityContextHolder.getContext().getAuthentication().getAuthorities());
		
		log.info("----------> logged user: {}, roles: {}", user, roles.stream().collect(Collectors.joining(",")));
		
		InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("resource",false);
		
		String path = instanceInfo.getHomePageUrl();// + "resource";
		
		Message message = restTemplate.getForObject(path, Message.class);
		
		return message;
	}

    public Message fallbackMessage() {
        return new Message("Send default message");
      }

//    @HystrixCommand(fallbackMethod = "fallbackMessage")
	@Override
	public Message getMessageMQ() {

		String exampleMessage = "Hello from RabbitMQ!";
    	
		log.info("Sending message: {}", exampleMessage);
    	
		rabbitTemplate.convertAndSend(RabbitMqConfig.topicExchangeName, "com.sant.test", exampleMessage);
    	
    	return null;
	}
	
    

}
