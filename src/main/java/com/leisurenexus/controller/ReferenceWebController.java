package com.leisurenexus.controller;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.leisurenexus.service.Person;
import com.leisurenexus.service.Reference;
import com.leisurenexus.service.ReferenceRepository;
import com.leisurenexus.service.UserRepository;
import com.neovisionaries.i18n.LocaleCode;

import io.reactivex.rxjava3.internal.operators.flowable.FlowableMap;
import io.v47.tmdb.TmdbClient;
import io.v47.tmdb.api.MovieRequest;
import io.v47.tmdb.model.MovieDetails;
import io.v47.tmdb.model.MovieListResult;
import io.v47.tmdb.model.PaginatedListResults;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("ref")
@Log4j2
public class ReferenceWebController {
	private @Autowired ReferenceRepository refRepository;
	private @Autowired UserRepository userRepository;
	private @Autowired TmdbClient tmdbClient;

	/**
	 * Returns all references from a source
	 * 
	 * @param sourceId
	 * @param model
	 * @return
	 */
	@GetMapping("")
	public String list(@RequestParam(required = false) String searchQuery, Model model,
			@AuthenticationPrincipal OidcUser principal) {
		Person user = retrieveUserFromPrincipal(principal);
		log.info("Searching references for source:" + user.getId());

		model.addAttribute("user", user);
		Reference sourceRefExample = Reference.builder().source(user).build();

		Collection<Reference> sourceReferences = refRepository.findAll(Example.of(sourceRefExample));
		if (sourceReferences != null) {
			sourceReferences.parallelStream().forEach((ref) -> addTMDBMetadata(ref));
		}
		model.addAttribute("sourceReferences", sourceReferences);

		Collection<Reference> targetReferences = user.getReferences();
		if (targetReferences != null) {
			targetReferences.parallelStream().forEach((ref) -> addTMDBMetadata(ref));
		}
		model.addAttribute("targetReferences", targetReferences);

		if (searchQuery != null && !searchQuery.isEmpty()) {
			FlowableMap<String, PaginatedListResults<MovieListResult>> data = (FlowableMap<String, PaginatedListResults<MovieListResult>>) tmdbClient
					.getSearch().forMovies(searchQuery, null, LocaleCode.en_US, null, false, null, null);
			PaginatedListResults<MovieListResult> searchResults = (PaginatedListResults<MovieListResult>) data
					.blockingFirst();
			log.info(searchResults);
			log.info("Found " + searchResults.getTotalResults() + " in " + searchResults.getTotalPages());
			model.addAttribute("searchQuery", searchQuery);
			model.addAttribute("searchResults", searchResults);
		}

		model.addAttribute("users", userRepository.findAll());

		return "ref";
	}

	/**
	 * Add a reference
	 */
	@Transactional
	@GetMapping("/add")
	public RedirectView add(@RequestParam(required = true) Long tmdbId, String email,
			@AuthenticationPrincipal OidcUser principal) {
		Person user = retrieveUserFromPrincipal(principal);
		log.info("Add reference " + tmdbId + " for " + user.getId() + " to " + email);

		Optional<Person> source = userRepository.findById(user.getId());
		if (source.isPresent()) {
			Reference ref = null;
			Reference example = Reference.builder().source(source.get()).tmdbId(tmdbId).build();
			Optional<Reference> found = refRepository.findOne(Example.of(example));
			if (found.isEmpty()) {
				log.info("Reference saved: " + tmdbId + " for " + user.getId());
				ref = refRepository.save(example);
			} else {
				ref = found.get();
			}
			if (email != null && email != user.getEmail()) {
				Optional<Person> target = userRepository.findByEmail(email);
				if (target.isPresent()) {
					log.info("Add Reference " + tmdbId + " to target: " + email);
					if (ref.getTargets() == null) {
						ref.setTargets(new HashSet<>());
					}
					ref.getTargets().add(target.get());
					refRepository.save(ref);
				}
			}

		}
		return new RedirectView("");
	}

	/**
	 * Remove a reference from a source
	 */
	@Transactional
	@GetMapping("/remove")
	public RedirectView remove(@RequestParam(required = true) Long referenceId,
			@RequestParam(required = false) Long targetId) {
		log.info("Removing Reference " + referenceId + " to Target: " + targetId);
		Optional<Reference> found = refRepository.findById(referenceId);
		if (found.isPresent()) {
			Reference ref = found.get();
			if (targetId != null) {
				ref.setTargets(
						ref.getTargets().stream().filter(v -> !v.getId().equals(targetId)).collect(Collectors.toSet()));
				refRepository.save(ref);
			} else {
				// Remove reference and all targets
				refRepository.delete(ref);
			}
		}
		return new RedirectView("");
	}

	// TODO: Move TMDB client in ReferenceService with save and delete functions to
	// allow Transaction...
	private void addTMDBMetadata(Reference ref) {
		log.info("Searching " + ref.getTmdbId() + " in TMDB");

		FlowableMap<String, MovieDetails> data = (FlowableMap<String, MovieDetails>) tmdbClient.getMovie()
				.details(ref.getTmdbId().intValue(), LocaleCode.en_CA, MovieRequest.values());
		try {
			MovieDetails movie = (MovieDetails) data.blockingFirst();
			ref.setTitle(movie.getTitle());
			ref.setOverview(movie.getOverview());
			ref.setPosterPath(movie.getPosterPath());
			ref.setReleaseDate(movie.getReleaseDate());
			log.debug("Found movie: " + ref.getTitle() + " , " + movie.getBackdropPath());
		} catch (Throwable e) {
			log.error("An error occured while retrieving reference metadata of " + ref.getId(), e);
		}
	}

	// Move this method in UserService class
	private Person retrieveUserFromPrincipal(OidcUser principal) {
		log.info(principal);
		Optional<Person> user = userRepository.findByEmail(principal.getEmail());
		if (user.isEmpty()) {
			Person newOne = userRepository
					.save(Person.builder().email(principal.getEmail()).name(principal.getFullName()).build());
			return newOne;
		}
		return user.get();
	}
}
