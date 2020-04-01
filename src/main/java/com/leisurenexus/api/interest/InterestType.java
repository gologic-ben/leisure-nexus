package com.leisurenexus.api.interest;

import com.leisurenexus.api.recommandation.Boardgame;
import com.leisurenexus.api.recommandation.Movie;
import com.leisurenexus.api.recommandation.TvShow;
import com.leisurenexus.api.recommandation.Music;

public enum InterestType {
  MOVIE(Movie.class), BOARDGAME(Boardgame.class), TVSHOW(TvShow.class), MUSIC(Music.class);

  private Class<?> recommandationClass;

  InterestType(Class<?> recommandationClass) {
    this.recommandationClass = recommandationClass;
  }

  public Class<?> getRecommandationClass() {
    return recommandationClass;
  }
  
  
}
