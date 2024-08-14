package com.example.microservices.restful_web_services.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.microservices.restful_web_services.user.User;

import java.util.function.Predicate;

//Data Access Object
@Component
public class UserDaoService {

	private static List<User> users = new ArrayList<>();
	
	private static int usersCount = 0;
	
	static {
		users.add(new User(++usersCount, "Adam", LocalDate.now().minusYears(30)));
		users.add(new User(++usersCount, "Eve", LocalDate.now().minusYears(25)));
		users.add(new User(++usersCount, "Jim", LocalDate.now().minusYears(20)));
	}

	public List<User> findAll() {
		return users;
	}

	public User findOne(int id) {
		Predicate<? super User> predicate = user -> user.getId().equals(id);
		return users.stream().filter(predicate).findFirst().orElse(null);
	}
	
	public void deleteUserById(int id) {
		Predicate<? super User> predicate = user -> user.getId().equals(id);
		users.removeIf(predicate);
	}
	
	public User createUser(User user) {
		user.setId(++usersCount);
		users.add(user);
		return user;
	}

}
