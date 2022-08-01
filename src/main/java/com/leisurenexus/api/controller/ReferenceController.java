package com.leisurenexus.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leisurenexus.api.service.Reference;

import lombok.extern.java.Log;

@RestController
@Log
public class ReferenceController {
  private @Autowired com.leisurenexus.api.service.ReferenceRepository refRepository;

	@GetMapping("/ref")
	public Iterable<Reference> getReferences() {
		log.info("Find all references");
		return refRepository.findAll();
	}
/*
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
    // Filtering attributes
    if (StringUtils.isEmpty(user.getName())) {
      throw new IllegalArgumentException("name is mandatory");
    }
    if (user.getId() != null) {
      User savedUser = getUser(user.getId());
      savedUser.setName(user.getName());
      userRepository.save(savedUser);
      return savedUser.getId();
    } else {
      User newUser = new User(user.getName());
      userRepository.save(newUser);
      return newUser.getId();
    }

  }

  @GetMapping(path = "/users/{userId}/interests")
  public Set<Reference> getInterests(@PathVariable("userId") Long userId) throws UserNotFoundException {
    User user = getUser(userId);
    Set<Reference> list = user.getInterests();
    return list;
  }

  @PostMapping(path = "/users/{userId}/interests/{sourceId}/{interestType}")
  public void addSource(@PathVariable("userId") Long userId, @PathVariable("sourceId") Long sourceId, @PathVariable("interestType") String interestType) throws UserNotFoundException {
    User user = getUser(userId);
    User source = getUser(sourceId);
    if(user == source) {
      throw new IllegalArgumentException("Cannot have himself as a source");
    }
    InterestType interest = InterestType.valueOf(interestType);
    user.addInterest(new Reference(user, source, interest));
    userRepository.save(source);
    userRepository.save(user);
  }

  @DeleteMapping(path = "/users/{userId}/interests/{sourceId}/{interestType}")
  public void removeSource(@PathVariable("userId") Long userId, @PathVariable("sourceId") Long sourceId, @PathVariable("interestType") String interestType) throws UserNotFoundException {
    User user = getUser(userId);
    User source = getUser(sourceId);
    InterestType interest = InterestType.valueOf(interestType);
    user.removeInterest(new Reference(user, source, interest));
    userRepository.save(user);
  }

  @DeleteMapping(path = "/users/{userId}/recommandations/{recommandationId}")
  public void removeSource(@PathVariable("userId") Long userId, @PathVariable("recommandationId") Long recommandationId) throws UserNotFoundException {
    User user = getUser(userId);
    for (Iterator<Recommandation> iterator = user.getRecommandations().iterator(); iterator.hasNext();) {
      Recommandation r = iterator.next();
      if (r.getId().equals(recommandationId)) {
        iterator.remove();
      }
    }
    userRepository.save(user);
  }
*/
}
