package com.leisurenexus.api.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.leisurenexus.api.service.User;
import com.leisurenexus.api.service.UserNotFoundException;

import lombok.extern.java.Log;

@RestController
@Log
public class UserController {
	private @Autowired com.leisurenexus.api.service.UserRepository userRepository;

	@GetMapping("/users")
	public Iterable<User> getUsers() {
		return userRepository.findAll();
	}

	@GetMapping("/users/{id}")
	public User getUser(@PathVariable("id") Long id) throws UserNotFoundException {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			return user.get();
		}
		throw new UserNotFoundException();
	}

	@PostMapping(path = "/users")
	public Long addUser(@RequestBody User user) throws UserNotFoundException {
		log.info("calling addUser");

		// Filtering attributes
		if (StringUtils.hasLength(user.getName())) {
			throw new IllegalArgumentException("name is mandatory");
		}
		if (user.getId() != null) {
			User savedUser = getUser(user.getId());
			savedUser.setName(user.getName());
			userRepository.save(savedUser);
			return savedUser.getId();
		} else {
			User newUser = User.builder().name(user.getName()).build();
			userRepository.save(newUser);
			return newUser.getId();
		}

	}

}
