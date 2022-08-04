package com.leisurenexus.web.controller;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.leisurenexus.api.service.Reference;
import com.leisurenexus.api.service.User;

import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("web")
@Log4j2
public class ReferenceWebController {
	private @Autowired com.leisurenexus.api.service.ReferenceRepository refRepository;
	private @Autowired com.leisurenexus.api.service.UserRepository userRepository;

	/**
	 * Returns all references from a source
	 * 
	 * @param sourceId
	 * @param model
	 * @return
	 */
	@GetMapping("/ref")
	public String list(@RequestParam(required = true) Long id, Model model) {
		log.info("Searching references for " + id);

		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			model.addAttribute("user", user.get());
			Reference sourceRefExample = Reference.builder().source(user.get()).build();

			Collection<Reference> sourceReferences = refRepository.findAll(Example.of(sourceRefExample));
			model.addAttribute("sourceReferences", sourceReferences);

			Reference sharedRefExample = Reference.builder().target(user.get()).build();
			Collection<Reference> targetReferences = refRepository.findAll(Example.of(sharedRefExample));

			model.addAttribute("targetReferences", targetReferences);

		}
		return "ref";
	}

	/**
	 * Add a reference
	 */
	@Transactional
	@GetMapping("/add")
	public RedirectView add(@RequestParam(required = true) Long sourceId, @RequestParam(required = true) String externalId, Long targetId, RedirectAttributes attributes) {
		log.info("Add reference " + externalId + " for " + sourceId + " to " + targetId);

		Optional<User> source = userRepository.findById(sourceId);
		if (source.isPresent()) {
			Reference example = Reference.builder().source(source.get()).externalId(externalId).build();
			
			Optional<User> target = Optional.empty();
			if(targetId != null) {
				target = userRepository.findById(targetId);
				example.setTarget(target.get());
			}
			
			Optional<Reference>  ref = refRepository.findOne(Example.of(example));
			if(!ref.isPresent()) {
				log.info("Reference saved" + externalId + " for " + sourceId + " to " + targetId);
				refRepository.save(example);
			}
		}
		// redirect
		attributes.addAttribute("id", sourceId);
		return new RedirectView("ref");
	}

	/**
	 * Remove a reference from a source
	 */
	@Transactional
	@GetMapping("/remove")
	public RedirectView remove(@RequestParam(required = true) Long userId, @RequestParam(required = true) Long referenceId, RedirectAttributes attributes) {
		log.info("Removing reference " + referenceId);
		Optional<Reference> ref = refRepository.findById(referenceId);
		if (ref.isPresent()) {
			refRepository.delete(ref.get());
			
		}
		attributes.addAttribute("id", userId);
		return new RedirectView("ref");
	}

}
