package com.leisurenexus.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.leisurenexus.api.interest.Interest;
import com.leisurenexus.api.interest.InterestType;
import com.leisurenexus.api.recommandation.Movie;
import com.leisurenexus.api.user.User;
import com.leisurenexus.api.user.UserRepository;

@Component
public class DataLoader implements ApplicationRunner {
  private static Logger LOG = LoggerFactory.getLogger(DataLoader.class);
  
  UserRepository userRepository;

  @Autowired
  public DataLoader(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void run(ApplicationArguments args) {
    LOG.info("DataLoader::Loading data...");

    User ben = new User("ben");
    ben.addRecommandation(new Movie("titanic", "ABC"));
    ben.addRecommandation(new Movie("gremlins", "DEF"));
    
    User paul = new User("paul");
    paul.addRecommandation(new Movie("pulp fiction", "GHI"));
      
    ben.addInterest(new Interest(ben, paul, InterestType.MOVIE));
    
    userRepository.save(paul);
    userRepository.save(ben);

  }
}
