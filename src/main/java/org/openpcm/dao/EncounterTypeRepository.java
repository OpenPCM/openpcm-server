package org.openpcm.dao;

import org.openpcm.model.EncounterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EncounterTypeRepository extends PagingAndSortingRepository<EncounterType, Long> {

	Page<EncounterType> findByName(String name, Pageable pageable);
}
