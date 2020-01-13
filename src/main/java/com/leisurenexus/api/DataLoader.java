package com.leisurenexus.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.leisurenexus.api.model.Interest;
import com.leisurenexus.api.model.InterestEnum;
import com.leisurenexus.api.model.Movie;
import com.leisurenexus.api.model.User;
import com.leisurenexus.api.model.UserRepository;

@Component
public class DataLoader implements ApplicationRunner {

  UserRepository userRepository;

  @Autowired
  public DataLoader(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void run(ApplicationArguments args) {
    System.out.println("DataLoader::Loading data...");

    User ben = new User("ben");
    ben.addRecommandation(new Movie("titanic", "ABC"));
    ben.addRecommandation(new Movie("gremlins", "DEF"));
    
    User paul = new User("paul");
    paul.addRecommandation(new Movie("pulp fiction", "GHI"));
      
    ben.addInterest(new Interest(ben, paul, InterestEnum.MOVIE));
    
    userRepository.save(paul);
    userRepository.save(ben);

  }
}
