package com.leisurenexus.api.recommandation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Movie is a Recommandation of a Interest
 */
@Entity
@Table(name = "movie")
public class Movie extends Recommandation {

  @Column
  String imdb;

  public Movie() {
    super();
  }

  public Movie(String name, String imdb) {
    super();
    this.name = name;
    this.imdb = imdb;
  }
  
  public Movie(Long id, String name, String imdb) {
    super();
    this.setId(id);
    this.name = name;
    this.imdb = imdb;
  }

  public String getImdb() {
    return imdb;
  }

  public void setImdb(String imdb) {
    this.imdb = imdb;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((imdb == null) ? 0 : imdb.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    Movie other = (Movie) obj;
    if (imdb == null) {
      if (other.imdb != null)
        return false;
    } else if (!imdb.equals(other.imdb))
      return false;
    return true;
  }

}
