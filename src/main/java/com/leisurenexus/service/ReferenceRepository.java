package com.leisurenexus.service;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReferenceRepository extends JpaRepository<Reference, Long> {
	Optional<Reference> findByTmdbId(Long tmdbId);
	void deleteBySourceIdAndTmdbId(Long sourceId, Long tmdbId);
	
}
