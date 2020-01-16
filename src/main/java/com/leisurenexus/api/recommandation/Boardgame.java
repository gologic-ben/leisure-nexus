package com.leisurenexus.api.recommandation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.leisurenexus.api.interest.InterestType;

/**
 * Boardgame is a Recommandation of a Interest
 */
@Entity
@Table(name = "boardgame")
public class Boardgame extends Recommandation {

  /** reference of boardgame geek ID ex: "1406" = https://boardgamegeek.com/boardgame/1406/monopoly */
  @Column
  String bgg;

  public Boardgame() {
    super();
  }

  public Boardgame(String name, String bgg) {
    super();
    this.name = name;
    this.bgg = bgg;
  }
  
  public Boardgame(Long id, String name, String bgg) {
    super();
    this.setId(id);
    this.name = name;
    this.bgg = bgg;
  }

  public InterestType getType() {
    return InterestType.BOARDGAME;
  }
  
  public String getBgg() {
    return bgg;
  }

  public void setBgg(String bgg) {
    this.bgg = bgg;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((bgg == null) ? 0 : bgg.hashCode());
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
    Boardgame other = (Boardgame) obj;
    if (bgg == null) {
      if (other.bgg != null)
        return false;
    } else if (!bgg.equals(other.bgg))
      return false;
    return true;
  }

}
