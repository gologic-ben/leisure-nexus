package com.leisurenexus.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
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
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import com.leisurenexus.api.service.Reference;
import com.leisurenexus.api.service.User;
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
	private @Autowired com.leisurenexus.api.service.ReferenceRepository refRepository;
	private @Autowired com.leisurenexus.api.service.UserRepository userRepository;
	private @Autowired TmdbClient tmdbClient;

	/**
	 * Returns all references from a source
	 * 
	 * @param sourceId
	 * @param model
	 * @return
	 */
	@GetMapping("")
	public String list(Model model, @AuthenticationPrincipal OidcUser principal) {
		User user = retrieveUserFromPrincipal(principal);
		log.info("Searching references for source:" + user.getId());
		
		model.addAttribute("user", user);
		Reference sourceRefExample = Reference.builder().source(user).build();

		Collection<Reference> sourceReferences = refRepository.findAll(Example.of(sourceRefExample));
		sourceReferences.parallelStream().forEach((ref) -> addTMDBMetadata(ref));
		model.addAttribute("sourceReferences", sourceReferences);

		Reference sharedRefExample = Reference.builder().target(user).build();
		Collection<Reference> targetReferences = refRepository.findAll(Example.of(sharedRefExample));
		targetReferences.parallelStream().forEach((ref) -> addTMDBMetadata(ref));
		
		model.addAttribute("targetReferences", targetReferences);

		return "ref";
	}

	/**
	 * Add a reference
	 */
	@Transactional
	@GetMapping("/add")
	public RedirectView add(@RequestParam(required = true) Long sourceId, @RequestParam(required = true) Long tmdbId, Long targetId) {
		log.info("Add reference " + tmdbId + " for " + sourceId + " to " + targetId);

		Optional<User> source = userRepository.findById(sourceId);
		if (source.isPresent()) {
			Reference example = Reference.builder().source(source.get()).tmdbId(tmdbId).build();
			
			Optional<User> target = Optional.empty();
			if(targetId != null) {
				target = userRepository.findById(targetId);
				example.setTarget(target.get());
			}
			
			Optional<Reference>  ref = refRepository.findOne(Example.of(example));
			if(!ref.isPresent()) {
				log.info("Reference saved" + tmdbId + " for " + sourceId + " to " + targetId);
				refRepository.save(example);
			}
		}
		return new RedirectView("");
	}

	/**
	 * Remove a reference from a source
	 */
	@Transactional
	@GetMapping("/remove")
	public RedirectView remove(@RequestParam(required = true) Long referenceId) {
		log.info("Removing reference " + referenceId);
		Optional<Reference> ref = refRepository.findById(referenceId);
		if (ref.isPresent()) {
			refRepository.delete(ref.get());
			
		}
		return new RedirectView("");
	}
	
	/**
	 * Search for movies and returns a Movie object
	 * @param search
	 * @return
	 */
	@GetMapping("/search")
	public String SearchReferences(Model model, String query) {
		model.addAttribute("query", query);
		final List<MovieListResult> results = new ArrayList<>();
		model.addAttribute("results", new ReactiveDataDriverContextVariable(results, 100));
		
		Publisher<PaginatedListResults<MovieListResult>> data = tmdbClient.getSearch().forMovies("Jack+Reacher", null, null, null, null, null, null);
		log.info(data);
		model.addAttribute("data", new ReactiveDataDriverContextVariable(data, 100));
		data.subscribe(new Subscriber<PaginatedListResults<MovieListResult>>() {

			@Override
			public void onSubscribe(Subscription s) {
				log.info("onSubscribe");
				s.request(Integer.MAX_VALUE);
			}

			@Override
			public void onNext(PaginatedListResults<MovieListResult> t) {
				log.info("onNext: " + t);
				results.addAll(t.getResults());
			}

			@Override
			public void onError(Throwable t) {
				log.info("onError");
				
			}

			@Override
			public void onComplete() {
				log.info("onComplete");
				
			}
			
		});
		log.info("results " + results.size());
		return "search";
	}
	
	// TODO: Move TMDB client in ReferenceService with save and delete functions to allow Transaction...
	private void addTMDBMetadata(Reference ref) {
		log.info("Searching " + ref.getTmdbId() + " in TMDB");
		
		MovieDetailsSubscriber subscriber = new MovieDetailsSubscriber(ref);
		tmdbClient.getMovie().details(ref.getTmdbId().intValue(), LocaleCode.en_CA, MovieRequest.values()).subscribe(subscriber);
		try {
			ref = subscriber.get();
			log.debug("Found movie: " + ref.getTitle());		
		} catch (Throwable e) {
			log.error("An error occured while retrieving reference metadata of " + ref.getId(), e);
		}
	}
	
	// Move this method in UserService class 
	private User retrieveUserFromPrincipal(OidcUser principal) {
		log.info(principal);
		Optional<User> user = userRepository.findByEmail(principal.getEmail());
		if(user.isEmpty()) {
			User newOne = userRepository.save(User.builder().email(principal.getEmail()).name(principal.getFullName()).build());
			return newOne;
		}
		return user.get();
	}
	
	class MovieDetailsSubscriber implements Subscriber<MovieDetails> {
		private final CountDownLatch latch;
		private Reference ref;
		
		public MovieDetailsSubscriber(Reference ref) {
			this.latch = new CountDownLatch(1);
			this.ref = ref;
		}
		
		public Reference get() throws Throwable {
            return await();
        }
		
		@Override
		public void onSubscribe(Subscription s) {
			log.debug("onSubscribe:" + s.toString());
			s.request(Integer.MAX_VALUE);
		}

		@Override
		public void onNext(MovieDetails movie) {
			log.debug("onNext: " + movie);
			if(movie != null) {
				ref.setTitle(movie.getTitle());
				ref.setOverview(movie.getOverview());
				ref.setPosterPath(movie.getPosterPath());
				ref.setReleaseDate(movie.getReleaseDate());
				onComplete();
			}
		}

		@Override
		public void onError(Throwable t) {
			log.error("An error occured while searching for reference " + ref.getId(), t);
			onComplete();
		}

		@Override
		public void onComplete() {
			log.debug("onComplete");
			latch.countDown();
		}
		
		 public Reference await() throws Throwable {
            return await(1, TimeUnit.MINUTES);
        }

        public Reference await(final long timeout, final TimeUnit unit) throws Throwable {
            if (!latch.await(timeout, unit)) {
            	throw new RuntimeException("Publisher onComplete timed out");
            }
            return ref;
        }
	}

}
