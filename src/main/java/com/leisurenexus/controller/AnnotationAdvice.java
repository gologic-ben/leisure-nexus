package com.leisurenexus.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = {Controller.class, RestController.class})
public class AnnotationAdvice {

	@ModelAttribute("currentUser")
	public Map<String, Object> getCurrentUser(@AuthenticationPrincipal OidcUser principal) {
		if (principal != null) {
			return principal.getClaims();
		}
		return Collections.emptyMap();
	}
}