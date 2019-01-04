package com.sant.hms.ui.controllers;

import org.springframework.web.bind.annotation.GetMapping;

import com.sant.hms.ui.pojo.Message;

public interface GreetingController {

	@GetMapping("/message")
	Message getMessage();
	
	
}
