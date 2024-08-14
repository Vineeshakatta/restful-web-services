package com.example.microservices.restful_web_services.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.microservices.restful_web_services.dao.Name;
import com.example.microservices.restful_web_services.dao.PersonV1;
import com.example.microservices.restful_web_services.dao.PersonV2;

@RestController
public class VersioningPersonController {

	// version of output data without disturbing previous version. so, enduser can
	// use both versions without urge to use latest version
	@GetMapping("/v1/person")
	public PersonV1 getFirstVersionOfPerson() {
		return new PersonV1("Bob Charlie");
	}

	@GetMapping("/v2/person")
	public PersonV2 getSecondVersionOfPerson() {
		return new PersonV2(new Name("Bob", "Charlie"));
	}
	
	@GetMapping(path = "/person", params = "version=1")
	public PersonV1 getFirstVersionOfPersonRequestParameter() {
		return new PersonV1("Bob Charlie");
	}
	
	@GetMapping(path = "/person", params = "version=2")
	public PersonV2 getSecondVersionOfPersonRequestParameter() {
		return new PersonV2(new Name("Bob", "Charlie"));
	}
	
	@GetMapping(path = "/person/header", headers = "X-API-VERSION=1")
	public PersonV1 getFirstVersionOfPersonRequestHeader() {
		return new PersonV1("Bob Charlie");
	}
	
	//X-API-VERSION is same url headers=[X-API-VERSION=1]
	// this is custom headers versioning
	@GetMapping(path = "/person/header", headers = "X-API-VERSION=2")
	public PersonV2 getSecondVersionOfPersonRequestHeader() {
		return new PersonV2(new Name("Bob", "Charlie"));
	}
	
	// content negotiation or accept header is a media type versioning
	// SAME-URL produces=application/vnd.company.app-v1+json
	@GetMapping(path = "/person/accept", produces = "application/vnd.company.app-v1+json")
	public PersonV1 getFirstVersionOfPersonRequestAcceptHeader() {
		return new PersonV1("Bob Charlie");
	}
	
	@GetMapping(path = "/person/accept", produces = "application/vnd.company.app-v2+json")
	public PersonV2 getSecondVersionOfPersonRequestAcceptHeader() {
		return new PersonV2(new Name("Bob", "Charlie"));
	}
}
