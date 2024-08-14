package com.example.microservices.restful_web_services.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.microservices.restful_web_services.user.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
