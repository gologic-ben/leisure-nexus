package com.leisurenexus.api;

import java.util.Iterator;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.leisurenexus.api.model.Interest;
import com.leisurenexus.api.model.InterestEnum;
import com.leisurenexus.api.model.Movie;
import com.leisurenexus.api.model.Recommandation;
import com.leisurenexus.api.model.User;
import com.leisurenexus.api.model.UserNotFoundException;

@RestController
public class ApiController {

  private @Autowired com.leisurenexus.api.model.UserRepository userRepository;

  @GetMapping("/users")
  public Iterable<User> users() {
    return userRepository.findAll();
  }

  @GetMapping("/users/{id}")
  public User user(@PathVariable("id") Long id) throws UserNotFoundException {
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) {
      return user.get();
    }
    throw new UserNotFoundException();
  }

  @PostMapping(path = "/users")
  public void addUser(@RequestBody User user) throws UserNotFoundException {
    // Filtering attributes
    if(StringUtils.isEmpty(user.getName())) {
      throw new IllegalArgumentException("name is mandatory");
    }
    if(user.getId() != null) {
      User savedUser = user(user.getId());
      savedUser.setName(user.getName());
      userRepository.save(savedUser);
    } else {
      User newUser = new User(user.getName());
      userRepository.save(newUser);
    }
  }

  @PostMapping(path = "/users/{userId}/{sourceId}/{type}")
  public void addSource(@PathVariable("userId") Long userId, @PathVariable("sourceId") Long sourceId, @PathVariable("type") String type) throws UserNotFoundException {
    User user = user(userId);
    User source = user(sourceId);
    InterestEnum interest = InterestEnum.valueOf(type);
    user.addInterest(new Interest(user, source, interest));
    userRepository.save(source);
    userRepository.save(user);
  }

  @DeleteMapping(path = "/users/{userId}/{sourceId}/{type}")
  public void removeSource(@PathVariable("userId") Long userId, @PathVariable("sourceId") Long sourceId, @PathVariable("type") String type) throws UserNotFoundException {
    User user = user(userId);
    User source = user(sourceId);
    InterestEnum interest = InterestEnum.valueOf(type);
    user.removeInterest(new Interest(user, source, interest));
    userRepository.save(user);
  }
  
  @PostMapping(path = "/users/{userId}/{type}")
  public void addRecommandation(@PathVariable("userId") Long userId, @PathVariable("type") String type, @RequestBody JsonNode recommandation) throws UserNotFoundException {
    User user = user(userId);
    // Type allow to retrieve "Recommandation" sub-class
    InterestEnum interest = InterestEnum.valueOf(type);
    if(interest.equals(InterestEnum.MOVIE)) {
      // TODO: Find movie in persistence by his attributes 
      Movie movie = new Movie(recommandation.get("name").textValue(), recommandation.get("imdb").textValue());
      user.addRecommandation(movie);
      userRepository.save(user);
    }
    
  }
  
  @DeleteMapping(path = "/users/{userId}/{recommandationId}")
  public void removeSource(@PathVariable("userId") Long userId, @PathVariable("recommandationId") Long recommandationId) throws UserNotFoundException {
    User user = user(userId);
    for (Iterator<Recommandation> iterator = user.getRecommandations().iterator(); iterator.hasNext();) {
      Recommandation r = iterator.next();
      if(r.getId().equals(recommandationId)) {
        iterator.remove();
      }
    }    
    userRepository.save(user);
  }
}
