package com.leisurenexus.api.service;

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

    User ben = User.builder().name("ben").build();
    User paul = User.builder().name("paul").build();
    User poulette = User.builder().name("poulette").build();
    
    Reference jurassic = Reference.builder().source(ben).target(paul).externalId("tt8041270").build();
    Reference titanic = Reference.builder().source(ben).target(paul).externalId("tt0120338").build();
    Reference ameliepoulin = Reference.builder().source(poulette).target(ben).externalId("tt0211915").build();
    Reference gb = Reference.builder().source(ben).target(poulette).externalId("tt0087332").build();
    Reference gremlins = Reference.builder().source(paul).target(ben).externalId("tt0087363").build();
    Reference terminator = Reference.builder().source(poulette).target(paul).externalId("tt0088247").build();
    
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
