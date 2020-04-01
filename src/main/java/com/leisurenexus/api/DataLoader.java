package com.leisurenexus.api;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.leisurenexus.api.interest.Interest;
import com.leisurenexus.api.interest.InterestType;
import com.leisurenexus.api.recommandation.Boardgame;
import com.leisurenexus.api.recommandation.Movie;
import com.leisurenexus.api.recommandation.Music;
import com.leisurenexus.api.recommandation.TvShow;
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

    // Generate random users
    // for each users Generate random movies and add recommandations
    // for each users add random users as interests (except himself)
    Double nbUsers = getRandomDoubleBetweenRange(5, 10);
    for (int i = 0; i <= nbUsers; i++) {
      User u = new User(generateName());
      for (int j = 0; j <= getRandomDoubleBetweenRange(0, 8); j++) {
        u.addRecommandation(new Movie(generateName(), ""));
      }
      for (int j = 0; j <= getRandomDoubleBetweenRange(0, 8); j++) {
        u.addRecommandation(new Boardgame(generateName(), ""));
      }
      for (int j = 0; j <= getRandomDoubleBetweenRange(0, 8); j++) {
        u.addRecommandation(new Music(generateName(), "", "", ""));
      }
      for (int j = 0; j <= getRandomDoubleBetweenRange(0, 8); j++) {
        u.addRecommandation(new TvShow(generateName(), ""));
      }
      userRepository.save(u);
    }

    List<User> users = (List<User>) userRepository.findAll();
    for (User owner : users) {
      Double nbUsersAsInterets = getRandomDoubleBetweenRange(0, 5);
      for (int i = 0; i <= nbUsersAsInterets; i++) {
        User movieSource = users.get(getRandomDoubleBetweenRange(0, users.size() - 1).intValue());
        if (movieSource != owner) {
          owner.addInterest(new Interest(owner, movieSource, InterestType.MOVIE));
          userRepository.save(movieSource);
          userRepository.save(owner);
        }
        User musicSource = users.get(getRandomDoubleBetweenRange(0, users.size() - 1).intValue());
        if (musicSource != owner) {
          owner.addInterest(new Interest(owner, musicSource, InterestType.MUSIC));
          userRepository.save(musicSource);
          userRepository.save(owner);
        }
        User tvShowSource = users.get(getRandomDoubleBetweenRange(0, users.size() - 1).intValue());
        if (tvShowSource != owner) {
          owner.addInterest(new Interest(owner, tvShowSource, InterestType.TVSHOW));
          userRepository.save(tvShowSource);
          userRepository.save(owner);
        }
        User boardgameSource = users.get(getRandomDoubleBetweenRange(0, users.size() - 1).intValue());
        if (boardgameSource != owner) {
          owner.addInterest(new Interest(owner, boardgameSource, InterestType.BOARDGAME));
          userRepository.save(boardgameSource);
          userRepository.save(owner);
        }
      }
    }



  }

  private static String[] Beginning = {"Kr", "Ca", "Ra", "Mrok", "Cru",
                                       "Ray", "Bre", "Zed", "Drak", "Mor", "Jag", "Mer", "Jar", "Mjol",
                                       "Zork", "Mad", "Cry", "Zur", "Creo", "Azak", "Azur", "Rei", "Cro",
                                       "Mar", "Luk"};
  private static String[] Middle = {"air", "ir", "mi", "sor", "mee", "clo",
                                    "red", "cra", "ark", "arc", "miri", "lori", "cres", "mur", "zer",
                                    "marac", "zoir", "slamar", "salmar", "urak"};
  private static String[] End = {"d", "ed", "ark", "arc", "es", "er", "der",
                                 "tron", "med", "ure", "zur", "cred", "mur"};

  private static Random rand = new Random();

  public static String generateName() {

    return Beginning[rand.nextInt(Beginning.length)] +
           Middle[rand.nextInt(Middle.length)] +
           End[rand.nextInt(End.length)];

  }

  public static Double getRandomDoubleBetweenRange(double min, double max) {
    Double x = (Math.random() * ((max - min) + 1)) + min;
    return x;
  }

}
