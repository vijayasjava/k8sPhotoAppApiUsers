package com.appsdeveloperblog.photoapp.api.users.ui.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RefreshScope
public class UsersController {

	
	private static final Logger log = LoggerFactory.getLogger(UsersController.class);

	
	@Autowired
	private Environment env;

	@GetMapping("/status/check")
	public String status() {
		return "application is up and running";
	}

}
