package com.leisurenexus.service.tmdb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class TmdbService {
	private @Autowired RestTemplate restTemplate;
	private @Value("${tmdb-client.api-key}") String apiKey;

	public Movie get(Long tmdbId) {
		String url = "https://api.themoviedb.org/3/movie/" + tmdbId + "?api_key=" + apiKey + "&language=en-US";
		log.info("get movie " + tmdbId);
		Movie movie = restTemplate.getForObject(url, Movie.class);
		log.debug(movie.getId());
		return movie;
	}

	public List<Movie> search(String queryString, Integer page) {
		String url = "https://api.themoviedb.org/3/search/movie?api_key=" + apiKey + "&language=en-US&query="
				+ queryString + "&page=" + page + "&include_adult=false";
		log.info("Searching for " + queryString + ", page = " + page);
		List<Movie> searchResults = new ArrayList<>();

		Map<String, Object> response = restTemplate.getForObject(url, Map.class);
		for (Map.Entry entry : response.entrySet()) {
			if (entry.getKey().equals("results")) {
				ObjectMapper mapper = new ObjectMapper();
				searchResults = mapper.convertValue(entry.getValue(), new TypeReference<List<Movie>>() {
				});
			}
		}
		return searchResults;
	}

}
