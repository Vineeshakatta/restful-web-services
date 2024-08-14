package com.example.microservices.restful_web_services.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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

import com.example.microservices.restful_web_services.dao.Post;
import com.example.microservices.restful_web_services.exception.UserNotFoundException;
import com.example.microservices.restful_web_services.repository.PostRepository;
import com.example.microservices.restful_web_services.repository.UserRepository;
import com.example.microservices.restful_web_services.user.User;

import jakarta.validation.Valid;

@RestController
public class UserJPAController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;

	@GetMapping("/jpa/users")
	public List<User> retrieveAllUsers() {
		return userRepository.findAll();
	}

	// to implement hyper links to data we should use
	// EntityModel and WebMvcLinkBuilder
	@GetMapping("/jpa/users/{id}")
	public EntityModel<User> retrieveUserDetails(@PathVariable int id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isEmpty()) {
			throw new UserNotFoundException("id:" + id);
		}
		EntityModel<User> entityModel = EntityModel.of(user.get());
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers()); // method name of list all users
																						// retrieveAllUsers()
		entityModel.add(link.withRel("all-users"));
		return entityModel;
	}

	@PostMapping("/jpa/users")
	public ResponseEntity<User> createUserDetails(@Valid @RequestBody User user) {
		User savedUser = userRepository.save(user);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/jpa/users/{id}")
	public void deleteUser(@PathVariable int id) {
		userRepository.deleteById(id);
	}
	
	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> deletePostsForUser(@PathVariable int id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isEmpty()) {
			throw new UserNotFoundException("id:" + id);
		}
		return user.get().getPosts();
	}
	
	@PostMapping("/jpa/users/{id}/posts")
	public ResponseEntity<Post> createPostsForUser(@PathVariable int id, @Valid @RequestBody Post post) {
		Optional<User> user = userRepository.findById(id);
		if (user.isEmpty()) {
			throw new UserNotFoundException("id:" + id);
		}
		post.setUser(user.get());
		Post savedPost = postRepository.save(post);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedPost.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

}
