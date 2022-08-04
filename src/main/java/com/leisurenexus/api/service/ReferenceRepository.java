package com.leisurenexus.api.service;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReferenceRepository extends JpaRepository<Reference, Long> {
	void deleteBySourceIdAndExternalId(Long sourceId, String externalId);
}
