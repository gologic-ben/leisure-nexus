package com.leisurenexus.api.interest;

import com.leisurenexus.api.recommandation.Boardgame;
import com.leisurenexus.api.recommandation.Movie;

public enum InterestType {
  MOVIE(Movie.class), BOARDGAME(Boardgame.class);

  private Class<?> recommandationClass;

  InterestType(Class<?> recommandationClass) {
    this.recommandationClass = recommandationClass;
  }

  public Class<?> getRecommandationClass() {
    return recommandationClass;
  }
  
  
}
