package com.leisurenexus.api.service;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

	// External reference id:IMDB movie identifier like tt8041270
	private String externalId;

}
