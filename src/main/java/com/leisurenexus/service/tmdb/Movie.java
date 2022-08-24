package com.leisurenexus.service.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Movie {
	private Integer id;
	private @JsonProperty("poster_path") String posterPath;
	private Boolean adult;
	private String overview;
	private @JsonProperty("release_date") String releaseDate;
	private @JsonProperty("original_title") String originalTitle;
	private @JsonProperty("original_language") String originalLanguage;
	private String title;
	private @JsonProperty("backdrop_path") String backdropPath;
	private Double popularity;
	private @JsonProperty("vote_count") Integer voteCount;
	private Boolean video;
	private @JsonProperty("vote_average") Double voteAverage;
	private String[] genre_ids;
}
