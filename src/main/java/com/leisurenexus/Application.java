package com.leisurenexus;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.web.client.RestTemplate;

import com.leisurenexus.service.Person;
import com.leisurenexus.service.Reference;
import com.leisurenexus.service.ReferenceRepository;
import com.leisurenexus.service.UserRepository;

import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@Log4j2
public class Application {
	private @Autowired UserRepository userRepository;
	private @Autowired ReferenceRepository refRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	@EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
		log.info("DataLoader::Loading data...");

	    final Person ben = Person.builder().name("ben").email("benouille@gmail.com").build();
	    final Person paul = Person.builder().name("paul").email("pm.adam@gmail.com").build();
	    final Person poulette = Person.builder().name("poulette").email("annelaurebarbier@gmail.com").build();
	    
	    Reference jurassic = Reference.builder().source(ben).targets(new HashSet<>(Arrays.asList(paul))).tmdbId(507086L).build();
	    Reference titanic = Reference.builder().source(ben).targets(new HashSet<>(Arrays.asList(paul))).tmdbId(597L).build();
	    Reference ameliepoulin = Reference.builder().source(poulette).targets(new HashSet<>(Arrays.asList(ben))).tmdbId(194L).build();
	    Reference gb = Reference.builder().source(ben).targets(new HashSet<>(Arrays.asList(poulette))).tmdbId(620L).build();
	    Reference gremlins = Reference.builder().source(paul).targets(new HashSet<>(Arrays.asList(ben))).tmdbId(927L).build();
	    Reference terminator = Reference.builder().source(poulette).targets(new HashSet<>(Arrays.asList(paul))).tmdbId(218L).build();
	    
	    Iterable<Person> all = userRepository.findAll();
	    all.forEach(p->{
	    	if(p.getEmail().equals(ben.getEmail())) { ben.setId(p.getId());}
	    	if(p.getEmail().equals(paul.getEmail())) { paul.setId(p.getId());}
	    	if(p.getEmail().equals(poulette.getEmail())) { poulette.setId(p.getId());}
	    });
	    
	    if( ben.getId() == null) userRepository.save(ben);
	    if( paul.getId() == null) userRepository.save(paul);
	    if( poulette.getId() == null) userRepository.save(poulette);

	    if(refRepository.findAllByTmdbId(jurassic.getTmdbId()).isEmpty()) refRepository.save(jurassic);
	    if(refRepository.findAllByTmdbId(titanic.getTmdbId()).isEmpty()) refRepository.save(titanic);
	    if(refRepository.findAllByTmdbId(ameliepoulin.getTmdbId()).isEmpty()) refRepository.save(ameliepoulin);
	    if(refRepository.findAllByTmdbId(gb.getTmdbId()).isEmpty()) refRepository.save(gb);
	    if(refRepository.findAllByTmdbId(gremlins.getTmdbId()).isEmpty()) refRepository.save(gremlins);
	    if(refRepository.findAllByTmdbId(terminator.getTmdbId()).isEmpty()) refRepository.save(terminator);
	    
	    log.info("DataLoader::Data loaded...");    
    }
	
}
