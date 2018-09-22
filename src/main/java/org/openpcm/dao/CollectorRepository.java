package org.openpcm.dao;

import java.util.Optional;

import org.openpcm.model.Collector;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CollectorRepository extends PagingAndSortingRepository<Collector, Long> {

	Optional<Collector> findByName(String name);

	@Query(value = "select c from Collector c where c.attributes[?1] = ?2")
	Page<Collector> findByAttributesNameAndValue(String name, String value, Pageable pageable);
}
