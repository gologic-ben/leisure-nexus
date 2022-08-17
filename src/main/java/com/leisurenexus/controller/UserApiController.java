package com.leisurenexus.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leisurenexus.service.Person;
import com.leisurenexus.service.UserNotFoundException;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("api")
@Log4j2
public class UserApiController {
	private @Autowired com.leisurenexus.service.UserRepository userRepository;

	@GetMapping("/users")
	public Iterable<Person> getUsers() {
		return userRepository.findAll();
	}

	@GetMapping("/users/{id}")
	public Person getUser(@PathVariable("id") Long id) throws UserNotFoundException {
		Optional<Person> user = userRepository.findById(id);
		if (user.isPresent()) {
			return user.get();
		}
		throw new UserNotFoundException();
	}

	@PostMapping(path = "/users")
	public Long addUser(@RequestBody Person user) throws UserNotFoundException {
		log.info("calling addUser");

		// Filtering attributes
		if (StringUtils.hasLength(user.getName())) {
			throw new IllegalArgumentException("name is mandatory");
		}
		if (user.getId() != null) {
			Person savedUser = getUser(user.getId());
			savedUser.setName(user.getName());
			userRepository.save(savedUser);
			return savedUser.getId();
		} else {
			Person newUser = Person.builder().name(user.getName()).build();
			userRepository.save(newUser);
			return newUser.getId();
		}

	}

}
