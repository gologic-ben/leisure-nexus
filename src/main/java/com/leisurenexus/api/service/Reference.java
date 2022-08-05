package com.leisurenexus.api.service;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@EntityListeners(AuditListener.class)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Reference {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "source_id", nullable = false)
	User source;

	@ManyToOne
	@JoinColumn(name = "target_id")
	User target;

	// TMBD movie identifier like 284053
	private Long tmdbId;
	
	// Transient information received from rest api
	private @Transient String title;
	private @Transient String overview;
	private @Transient String posterPath;
	private @Transient LocalDate releaseDate;
}
