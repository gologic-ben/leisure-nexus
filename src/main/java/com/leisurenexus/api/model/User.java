package com.leisurenexus.api.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private String name;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "recommandation_users", joinColumns = @JoinColumn(name = "users_id"), inverseJoinColumns = @JoinColumn(name = "recommandation_id"))
  private Set<Recommandation> recommandations = new HashSet<>();

  // Mapping to interests, allow a user to scan his interests
  @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
  private Set<Interest> interests;
  
  // Mapping to sources, allow a user to scan his sources (other users) of interests
  @OneToMany(mappedBy = "source", cascade = CascadeType.ALL)
  @JsonIgnore
  private Set<Interest> sources;

  public User() {
    super();
  }

  public User(String name) {
    super();
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<Recommandation> getRecommandations() {
    return recommandations;
  }

  public void setRecommandations(Set<Recommandation> recommandations) {
    this.recommandations = recommandations;
  }

  public void addRecommandation(Recommandation recommandation) {
    if (recommandations == null)
      recommandations = new HashSet<Recommandation>();
    if (!recommandations.contains(recommandation))
      recommandations.add(recommandation);
  }

  public void removeRecommandation(Recommandation recommandation) {
    if (recommandations == null)
      return;
    if (recommandations.contains(recommandation))
      recommandations.remove(recommandation);
  }

  public Set<Interest> getInterests() {
    return interests;
  }

  public void setInterests(Set<Interest> interests) {
    this.interests = interests;
  }

  public void addInterest(Interest interest) {
    if (interests == null)
      interests = new HashSet<Interest>();
    if (!interests.contains(interest))
      interests.add(interest);
  }

  public void removeInterest(Interest interest) {
    if (interests == null)
      return;
    if (interests.contains(interest))
      interests.remove(interest);
  }

}
