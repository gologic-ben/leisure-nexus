package com.leisurenexus.api;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.leisurenexus.api.interest.Interest;
import com.leisurenexus.api.interest.InterestType;
import com.leisurenexus.api.recommandation.Movie;
import com.leisurenexus.api.recommandation.Recommandation;
import com.leisurenexus.api.user.User;
import com.leisurenexus.api.user.UserNotFoundException;

@RestController
public class UserController {
  private static Logger LOG = LoggerFactory.getLogger(UserController.class);

  private @Autowired com.leisurenexus.api.user.UserRepository userRepository;

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
  public Set<Interest> getInterests(@PathVariable("userId") Long userId) throws UserNotFoundException {
    User user = getUser(userId);
    Set<Interest> list = user.getInterests();
    return list;
  }

  @PostMapping(path = "/users/{userId}/interests/{sourceId}/{interestType}")
  public void addSource(@PathVariable("userId") Long userId, @PathVariable("sourceId") Long sourceId, @PathVariable("interestType") String interestType) throws UserNotFoundException {
    User user = getUser(userId);
    User source = getUser(sourceId);
    InterestType interest = InterestType.valueOf(interestType);
    user.addInterest(new Interest(user, source, interest));
    userRepository.save(source);
    userRepository.save(user);
  }

  @DeleteMapping(path = "/users/{userId}/interests/{sourceId}/{interestType}")
  public void removeSource(@PathVariable("userId") Long userId, @PathVariable("sourceId") Long sourceId, @PathVariable("interestType") String interestType) throws UserNotFoundException {
    User user = getUser(userId);
    User source = getUser(sourceId);
    InterestType interest = InterestType.valueOf(interestType);
    user.removeInterest(new Interest(user, source, interest));
    userRepository.save(user);
  }

  /**
   * Return user recommandations
   * 
   * interestType: Allow to filter interest of recommandations
   * 
   * depth: Allow to search deeper of recommandations
   */
  @GetMapping(path = "/users/{userId}/recommandations")
  public Set<Recommandation> getRecommandations(@PathVariable("userId") Long userId, @RequestParam(required = false) String interestType,
                                                @RequestParam(required = false) Integer depth) throws UserNotFoundException {
    User user = getUser(userId);
    Set<Recommandation> list = user.getRecommandations();
    if (interestType != null) {
      LOG.info("Filtering recommandations " + interestType + " for " + user);
      InterestType filter = InterestType.valueOf(interestType);
      list = user.getRecommandations().stream().filter(r -> r.getType().equals(filter)).collect(Collectors.toSet());
    }

    // search deeper recommandations
    if (depth > 0) {
      LOG.info("Search recommandations deeper for " + user);
      depth = depth - 1;
      Set<Interest> interests = user.getInterests();
      for (Interest interest : interests) {
        Set<Recommandation> subLevel = getRecommandations(interest.getSource().getId(), interestType, depth);
        list.addAll(subLevel);
      }
    }
    return list;
  }

  @PostMapping(path = "/users/{userId}/recommandations/{interestType}")
  public void addRecommandation(@PathVariable("userId") Long userId, @PathVariable("recommandationType") String interestType, @RequestBody JsonNode recommandation) throws UserNotFoundException {
    User user = getUser(userId);
    // Type allow to retrieve "Recommandation" sub-class
    InterestType interest = InterestType.valueOf(interestType);
    if (interest.equals(InterestType.MOVIE)) {
      // TODO: Find movie in persistence by his attributes
      Movie movie = new Movie(recommandation.get("name").textValue(), recommandation.get("imdb").textValue());
      user.addRecommandation(movie);
      userRepository.save(user);
    }

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
}
