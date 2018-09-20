package org.openpcm.dao;

import org.openpcm.model.Facility;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FacilityRepository extends PagingAndSortingRepository<Facility, Long> {

	Facility findByName(String name);
}
