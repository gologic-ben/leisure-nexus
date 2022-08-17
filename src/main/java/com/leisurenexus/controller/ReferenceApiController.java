package com.leisurenexus.controller;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leisurenexus.service.Reference;
import com.leisurenexus.service.Person;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("api")
@Log4j2
public class ReferenceApiController {
	private @Autowired com.leisurenexus.service.ReferenceRepository repository;

	@GetMapping("/ref")
	public Collection<Reference> getReferences(@RequestParam(required = false) Long sourceId, @RequestParam(required = false) Long tmdbId) {
		if (sourceId == null && tmdbId == null) {
			return Collections.emptyList();
		}
		Person source = Person.builder().id(sourceId).build();
		Reference example = Reference.builder().source(source).tmdbId(tmdbId).build();

		log.info("Searching references for: " + example);

		return repository.findAll(Example.of(example));

	}

	// Add a reference to a source and optionaly to a target
	@PostMapping(path = "/ref")
	public ResponseEntity<Void> addReference(@RequestParam Long sourceId, @RequestParam(required = false) Long targetId, @RequestParam Long tmdbId) {
		// TODO: Check if sourceId equals to Authenticated User

		// check if not exist
		if (getReferences(sourceId, tmdbId).isEmpty()) {
			Person source = Person.builder().id(sourceId).build();
			
			try {
				Reference add = Reference.builder().source(source).tmdbId(tmdbId).build();
				repository.save(add);
			} catch(RuntimeException e) {
				log.error("An error occured while saving reference", e);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}
		}
		return ResponseEntity.ok(null);

	}

	@DeleteMapping(path = "/ref")
	public void remove(@RequestParam Long sourceId, @RequestParam Long tmdbId) {
		// TODO: Check if sourceId equals to Authenticated User
		repository.deleteBySourceIdAndTmdbId(sourceId, tmdbId);
	}

}
