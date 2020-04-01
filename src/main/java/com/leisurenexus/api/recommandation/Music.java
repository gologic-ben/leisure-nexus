package com.leisurenexus.api.recommandation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.leisurenexus.api.interest.InterestType;

/**
 * Music is a Recommandation of a Interest
 */
@Entity
@Table(name = "music")
public class Music extends Recommandation {

  @Column
  String album;
  @Column
  String artist;
  @Column
  String track;

  public Music() {
    super();
  }

  public Music(String name, String album, String artist, String track) {
    super();
    this.name = name;
    this.album = album;
    this.artist = artist;
    this.track = track;
  }

  public Music(Long id, String name, String album, String artist, String track) {
    super();
    this.setId(id);
    this.name = name;
    this.album = album;
    this.artist = artist;
    this.track = track;
  }

  public InterestType getType() {
    return InterestType.MOVIE;
  }

  public String getAlbum() {
    return album;
  }

  public void setAlbum(String album) {
    this.album = album;
  }

  public String getArtist() {
    return artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public String getTrack() {
    return track;
  }

  public void setTrack(String track) {
    this.track = track;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((album == null) ? 0 : album.hashCode());
    result = prime * result + ((artist == null) ? 0 : artist.hashCode());
    result = prime * result + ((track == null) ? 0 : track.hashCode());
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
    Music other = (Music) obj;
    if (album == null) {
      if (other.album != null)
        return false;
    } else if (!album.equals(other.album))
      return false;
    if (artist == null) {
      if (other.artist != null)
        return false;
    } else if (!artist.equals(other.artist))
      return false;
    if (track == null) {
      if (other.track != null)
        return false;
    } else if (!track.equals(other.track))
      return false;
    return true;
  }

}
