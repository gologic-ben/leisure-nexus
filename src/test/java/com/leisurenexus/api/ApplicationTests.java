package com.leisurenexus.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leisurenexus.api.interest.Interest;
import com.leisurenexus.api.interest.InterestType;
import com.leisurenexus.api.recommandation.Movie;
import com.leisurenexus.api.recommandation.Recommandation;
import com.leisurenexus.api.user.User;
import com.leisurenexus.api.user.UserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class ApplicationTests {
  private static Logger LOG = LoggerFactory.getLogger(ApplicationTests.class);

  private @Autowired UserController ctrl;
  private ObjectMapper mapper = new ObjectMapper();

  @Test
  public void contexLoads() throws Exception {
    LOG.info("contexLoads");
    assertThat(ctrl).isNotNull();
  }

  /**
   * Recommand leisures
   * 
   * Princess Leia recommend movies like Star Wars IV and Venom.
   * 
   * Princess Leia recommend board games like Catan and Monopoly.
   */
  @Test
  void testOwnerMovieRecommandations() throws Exception {
    LOG.info("testOwnerRecommandations");

    // Create leia
    Long leiaId = ctrl.addUser(new User("leia"));
    User leia = ctrl.getUser(leiaId);
    assertThat(leiaId).isEqualTo(leia.getId());

    // Add recommandations
    Long id = leia.getId();
    ctrl.addRecommandation(id, "MOVIE", mapper.readTree("{\"name\":\"Star Wars IV\",\"imdb\":\"tt0076759\"}"));
    ctrl.addRecommandation(id, "MOVIE", mapper.readTree("{\"name\":\"Venom\",\"imdb\":\"tt1270797\"}"));
    ctrl.addRecommandation(id, "BOARDGAME", mapper.readTree("{\"name\":\"Catan\",\"bgg\":\"13\"}"));
    ctrl.addRecommandation(id, "BOARDGAME", mapper.readTree("{\"name\":\"Monopoly\",\"bgg\":\"1406\"}"));

    // Assert that leai recommands movies: starwars and venom
    Set<Recommandation> list = ctrl.getRecommandations(id);
    assertThat(list.stream().filter(r -> Movie.class.isInstance(r)).map(r -> r.getName()).collect(Collectors.toList())).contains("Star Wars IV", "Venom");
  }

  /**
   * Discover new leisures
   * 
   * Princess Leia follows the recommendations of her friends *Tim* and *Bob*
   * 
   * Princess Leia can quickly discover new sources of leisure thanks to the recommendations of her friends.
   * 
   * And so on for all of her sources of interests.
   */
  @Test
  void testDirectRecommandations() throws Exception {
    LOG.info("testDirectRecommandations");

    // Create leia, tim and bob
    Long leiaId = ctrl.addUser(new User("leia"));
    Long timId = ctrl.addUser(new User("tim"));
    Long bobId = ctrl.addUser(new User("bob"));

    // Add tim, bob recommandations
    ctrl.addRecommandation(timId, "MOVIE", mapper.readTree("{\"name\":\"E.T. the Extra-Terrestrial\",\"imdb\":\"tt0083866\"}"));
    ctrl.addRecommandation(timId, "MOVIE", mapper.readTree("{\"name\":\"Independence Day\",\"imdb\":\"tt0116629\"}"));
    ctrl.addRecommandation(timId, "MOVIE", mapper.readTree("{\"name\":\"Ghostbusters\",\"imdb\":\"tt0087332\"}"));
    ctrl.addRecommandation(bobId, "MOVIE", mapper.readTree("{\"name\":\"The Mask\",\"imdb\":\"tt0110475\"}"));
    ctrl.addRecommandation(bobId, "MOVIE", mapper.readTree("{\"name\":\"Titanic\",\"imdb\":\"tt0120338\"}"));

    ctrl.addSource(leiaId, timId, "MOVIE");
    ctrl.addSource(leiaId, bobId, "MOVIE");
    
    // Assert that leia from her interests has direct friends movie recommandations
    Set<Interest> interests = ctrl.getInterests(leiaId);
    List<Long> friendIds = interests.stream().filter(i -> i.getType().equals(InterestType.MOVIE)).map(i -> i.getUser()).map(u -> u.getId()).distinct().collect(Collectors.toList());
    List<Recommandation> movies = new ArrayList<>();
    for (Long friendId : friendIds) {
      movies.addAll(ctrl.getRecommandations(friendId));
    }
    assertThat(movies.stream()
                     .filter(r -> Movie.class.isInstance(r))
                     .map(r -> r.getName())
                     .collect(Collectors.toList())).contains("E.T. the Extra-Terrestrial", "Independence Day", "Ghostbusters", "The Mask", "Titanic");



  }

  // TODO
  void testSubLevelRecommandations() throws Exception {
    LOG.info("testSubLevelRecommandations");
    
/*
    xien = repo.save(new User("xien"));
    tim.addInterest(new Interest(tim, xien, InterestType.MOVIE));
    tim.addRecommandation(new Movie("Gladiator", "tt0172495"));
    tim.addRecommandation(new Movie("Pulp Fiction", "tt0110912"));
    repo.save(tim);
    repo.save(xien);
*/


  }
}
