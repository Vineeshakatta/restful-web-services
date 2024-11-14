package com.example.microservices.restful_web_services.controller;

import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

//REST API
@RestController
public class HelloWorldController {
	
	@Autowired
	private MessageSource messageSource;

	@GetMapping(path = "/hello-world")
	public String helloWorld() {
		return "hello world";
	}
	
	@GetMapping(path = "/hello-world-bean")
	public HelloWorldBean helloWorldBean() {
		return new HelloWorldBean("hello world");
	}
	
	@GetMapping(path = "/hello-world/path-variable/{name}")
	public HelloWorldBean helloWorldBean(@PathVariable String name) {
		return new HelloWorldBean(String.format("hello world, %s", name));
	}
	
	@GetMapping(path = "/hello-world-internationalized")
	public String helloWorldInternationalized() {
		Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage("good.morning.message", null, "Default Message", locale);
	}
	
	@PostMapping(path = "/getMeaning")
    public ResponseEntity<String> getMeaning(@RequestParam("word") String word) {
        String apiUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;
        RestTemplate restTemplate = new RestTemplate();
        
        try {
            String response = restTemplate.getForObject(apiUrl, String.class);
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONArray meaningsArray = jsonObject.getJSONArray("meanings");
            JSONObject firstMeaning = meaningsArray.getJSONObject(0);
            JSONArray definitionsArray = firstMeaning.getJSONArray("definitions");
            String meaning = definitionsArray.getJSONObject(0).getString("definition");

            return ResponseEntity.ok(meaning);
        } catch (Exception e) {
            return ResponseEntity.ok("No meaning found for this word or error retrieving the meaning.");
        }
    }
}
