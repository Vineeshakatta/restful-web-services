package com.example.microservices.restful_web_services.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface ImageGeneratorService {
	
	public ResponseEntity<String> generateImage(@RequestParam("word") String word);

}
