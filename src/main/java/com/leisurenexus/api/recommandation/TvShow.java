package com.leisurenexus.api.recommandation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.leisurenexus.api.interest.InterestType;

/**
 * TvShow is a Recommandation of a Interest
 */
@Entity
@Table(name = "tvshow")
public class TvShow extends Recommandation {

  @Column
  String imdb;

  public TvShow() {
    super();
  }

  public TvShow(String name, String imdb) {
    super();
    this.name = name;
    this.imdb = imdb;
  }

  public TvShow(Long id, String name, String imdb) {
    super();
    this.setId(id);
    this.name = name;
    this.imdb = imdb;
  }

  public InterestType getType() {
    return InterestType.TVSHOW;
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
    TvShow other = (TvShow) obj;
    if (imdb == null) {
      if (other.imdb != null)
        return false;
    } else if (!imdb.equals(other.imdb))
      return false;
    return true;
  }

}
