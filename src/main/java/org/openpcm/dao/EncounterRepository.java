package org.openpcm.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.openpcm.model.Encounter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EncounterRepository extends PagingAndSortingRepository<Encounter, Long> {

    Optional<Encounter> findByTitle(String title);

    @EntityGraph(value = "Encounter.attributes", type = EntityGraph.EntityGraphType.LOAD)
    List<Encounter> findByAttributesKeyAndAttributesValue(String key, String value);

    Page<Encounter> findByTimestampBetweenAndTypes_NameContaining(Date startTime, Date endTime, String type, Pageable pageable);

    Page<Encounter> findByTimestampBetween(Date startTime, Date endTime, Pageable pageable);

    Page<Encounter> findByTimestampBetweenAndPatientId(Date startTime, Date endTime, Long patientId, Pageable pageable);

    @Override
    @EntityGraph(value = "Encounter.attributes", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Encounter> findById(Long id);

    @Override
    @EntityGraph(value = "Encounter.attributes", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<Encounter> findAll();

    @Override
    @EntityGraph(value = "Encounter.attributes", type = EntityGraph.EntityGraphType.LOAD)
    Page<Encounter> findAll(Pageable pageable);

    @Override
    @EntityGraph(value = "Encounter.attributes", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<Encounter> findAll(Sort sort);

    @Override
    @EntityGraph(value = "Encounter.attributes", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<Encounter> findAllById(Iterable<Long> ids);
}
