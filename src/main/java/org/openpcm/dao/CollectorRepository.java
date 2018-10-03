package org.openpcm.dao;

import java.util.List;
import java.util.Optional;

import org.openpcm.model.Collector;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CollectorRepository extends PagingAndSortingRepository<Collector, Long> {

    Optional<Collector> findByName(String name);

    @EntityGraph(value = "Collector.attributes", type = EntityGraph.EntityGraphType.LOAD)
    List<Collector> findByAttributesKeyAndAttributesValue(String key, String value);

    @Override
    @EntityGraph(value = "Collector.attributes", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Collector> findById(Long id);

    @Override
    @EntityGraph(value = "Collector.attributes", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<Collector> findAll();

    @Override
    @EntityGraph(value = "Collector.attributes", type = EntityGraph.EntityGraphType.LOAD)
    Page<Collector> findAll(Pageable pageable);

    @Override
    @EntityGraph(value = "Collector.attributes", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<Collector> findAll(Sort sort);

    @Override
    @EntityGraph(value = "Collector.attributes", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<Collector> findAllById(Iterable<Long> ids);
}
