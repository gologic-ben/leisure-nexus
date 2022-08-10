package com.leisurenexus.api.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class DataLoader implements ApplicationRunner {
  UserRepository userRepository;
  ReferenceRepository refRepository;

  @Autowired
  public DataLoader(UserRepository userRepository, ReferenceRepository refRepository) {
    this.userRepository = userRepository;
    this.refRepository = refRepository;
  }

  public void run(ApplicationArguments args) {
    log.info("DataLoader::Loading data...");

    User ben = User.builder().name("ben").email("benouille@gmail.com").build();
    User paul = User.builder().name("paul").email("pm.adam@gmail.com").build();
    User poulette = User.builder().name("poulette").email("annelaurebarbier@gmail.com").build();
    
    Reference jurassic = Reference.builder().source(ben).targets(new HashSet<>(Arrays.asList(paul))).tmdbId(507086L).build();
    Reference titanic = Reference.builder().source(ben).targets(new HashSet<>(Arrays.asList(paul))).tmdbId(597L).build();
    Reference ameliepoulin = Reference.builder().source(poulette).targets(new HashSet<>(Arrays.asList(ben))).tmdbId(194L).build();
    Reference gb = Reference.builder().source(ben).targets(new HashSet<>(Arrays.asList(poulette))).tmdbId(620L).build();
    Reference gremlins = Reference.builder().source(paul).targets(new HashSet<>(Arrays.asList(ben))).tmdbId(927L).build();
    Reference terminator = Reference.builder().source(poulette).targets(new HashSet<>(Arrays.asList(paul))).tmdbId(218L).build();
    
    userRepository.save(ben);
    userRepository.save(paul);
    userRepository.save(poulette);

    refRepository.save(jurassic);
    refRepository.save(titanic);
    refRepository.save(ameliepoulin);
    refRepository.save(gb);
    refRepository.save(gremlins);
    refRepository.save(terminator);

    log.info("DataLoader::Data loaded...");    
  }
/*
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
*/
  
}
