package com.example.microservices.restful_web_services.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.microservices.restful_web_services.dao.UserDaoService;
import com.example.microservices.restful_web_services.exception.UserNotFoundException;
import com.example.microservices.restful_web_services.user.User;

import jakarta.validation.Valid;

@RestController
public class UserController {

	@Autowired
	private UserDaoService service;

	@GetMapping("/users")
	public List<User> retrieveAllUsers() {
		return service.findAll();
	}

	// to implement hyper links to data we should use
	// EntityModel and WebMvcLinkBuilder
	@GetMapping("/users/{id}")
	public EntityModel<User> retrieveUserDetails(@PathVariable int id) {
		User user = service.findOne(id);
		if (user == null) {
			throw new UserNotFoundException("id:" + id);
		}
		EntityModel<User> entityModel = EntityModel.of(user);
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers()); // method name of list all users retrieveAllUsers()
		entityModel.add(link.withRel("all-users"));
		return entityModel;
	}

	@PostMapping("/users")
	public ResponseEntity<User> createUserDetails(@Valid @RequestBody User user) {
		User savedUser = service.createUser(user);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable int id) {
		service.deleteUserById(id);
	}

}
