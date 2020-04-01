package com.leisurenexus.api.interest;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.leisurenexus.api.user.User;

/**
 * Interest belongs to a user, contains a list of source which have recommandations.
 */
@Entity
@Table(name = "interest")
public class Interest implements Serializable {

  private static final long serialVersionUID = 6226662096874216090L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Enumerated(EnumType.STRING)
  private InterestType type;

  @ManyToOne
  @JoinColumn
  @JsonIgnore
  private User user;

  @ManyToOne
  @JoinColumn
  @JsonIgnoreProperties("interests") // Allow to skip JSON view of sub-attributes "interests" inside
                                     // entity source
  private User source;

  public Interest() {
    super();
  }

  public Interest(User user, User source, InterestType type) {
    super();
    this.user = user;
    this.source = source;
    this.type = type;
  }

  public InterestType getType() {
    return type;
  }

  public void setType(InterestType type) {
    this.type = type;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public User getSource() {
    return source;
  }

  public void setSource(User source) {
    this.source = source;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((source == null) ? 0 : source.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    result = prime * result + ((user == null) ? 0 : user.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Interest other = (Interest) obj;
    if (source == null) {
      if (other.source != null)
        return false;
    } else if (other.source == null) {
      if (source != null)
        return false;
    } else if (!source.getId().equals(other.source.getId()))
      return false;
    if (type != other.type)
      return false;
    if (user == null) {
      if (other.user != null)
        return false;
    } else if (other.user == null) {
      if (user != null)
        return false;
    } else if (!user.getId().equals(other.user.getId()))
      return false;
    return true;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Interest [id=");
    builder.append(id);
    builder.append(", type=");
    builder.append(type);
    builder.append(", user=");
    builder.append(user.getName());
    builder.append(", source=");
    builder.append(source.getName());
    builder.append("]");
    return builder.toString();
  }

}
