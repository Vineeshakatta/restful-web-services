package com.example.microservices.restful_web_services.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.microservices.restful_web_services.config.LimitServiceConfiguration;
import com.example.microservices.restful_web_services.dao.Limits;

@RestController
public class LimitsController {

	@Autowired
	private LimitServiceConfiguration limitServiceconfiguration;

	@GetMapping("/limits")
	public Limits retrieveLimits() {
		// getting values from @ConfigurationProperties application properties
		return new Limits(limitServiceconfiguration.getMinimum(), limitServiceconfiguration.getMaximum());
	}

}
