package com.appsdeveloperblog.photoapp.api.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
//@EnableDiscoveryClient
//@EnableFeignClients
//@EnableCircuitBreaker

public class PhotoAppApiUsersApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		  SpringApplication.run(PhotoAppApiUsersApplication.class, args);
	}

	  @Override
	  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	    return application.sources(PhotoAppApiUsersApplication.class);
	  }
	
}
