package com.example.microservices.restful_web_services.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.microservices.restful_web_services.dao.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {

}
