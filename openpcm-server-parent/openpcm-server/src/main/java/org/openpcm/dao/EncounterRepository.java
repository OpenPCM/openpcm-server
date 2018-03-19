package org.openpcm.dao;

import java.util.Date;
import java.util.Optional;

import org.openpcm.model.Encounter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EncounterRepository extends PagingAndSortingRepository<Encounter, Long> {

	Optional<Encounter> findByTitle(String title);

	Page<Encounter> findByTimestampBetweenAndTypes_NameContaining(Date startTime, Date endTime, String type,
			Pageable pageable);

	Page<Encounter> findByTimestampBetween(Date startTime, Date endTime, Pageable pageable);

	Page<Encounter> findByTimestampBetweenAndPatientId(Date startTime, Date endTime, Long patientId, Pageable pageable);
}
