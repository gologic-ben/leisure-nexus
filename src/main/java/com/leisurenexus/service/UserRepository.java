package com.leisurenexus.service;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Person, Long> {
	Optional<Person> findByEmail(String email);
}
