package org.openpcm.dao;

import java.util.Date;
import java.util.Optional;

import org.openpcm.model.ObservationSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ObservationSetRepository extends PagingAndSortingRepository<ObservationSet, Long> {

    Page<ObservationSet> findByOrigin(String origin, Pageable pageable);

    Page<ObservationSet> findByOriginAndTimestampBetween(String origin, Date startTime, Date endTime, Pageable pageable);

    @Override
    @EntityGraph(value = "ObservationSet.attributes", type = EntityGraph.EntityGraphType.LOAD)
    Optional<ObservationSet> findById(Long id);

    @Override
    @EntityGraph(value = "ObservationSet.attributes", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<ObservationSet> findAll();

    @Override
    @EntityGraph(value = "ObservationSet.attributes", type = EntityGraph.EntityGraphType.LOAD)
    Page<ObservationSet> findAll(Pageable pageable);

    @Override
    @EntityGraph(value = "ObservationSet.attributes", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<ObservationSet> findAll(Sort sort);

    @Override
    @EntityGraph(value = "ObservationSet.attributes", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<ObservationSet> findAllById(Iterable<Long> ids);
}
