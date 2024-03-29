package com.leisurenexus.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReferenceRepository extends JpaRepository<Reference, Long> {
	List<Reference> findAllByTmdbId(Long tmdbId);
	void deleteBySourceIdAndTmdbId(Long sourceId, Long tmdbId);
	
}
