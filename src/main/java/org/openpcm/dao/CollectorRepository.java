package org.openpcm.dao;

import java.util.List;
import java.util.Optional;

import org.openpcm.model.Collector;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CollectorRepository extends PagingAndSortingRepository<Collector, Long> {

    Optional<Collector> findByName(String name);

    @EntityGraph(value = "Collector.attributes", type = EntityGraph.EntityGraphType.LOAD)
    List<Collector> findByAttributesKeyAndAttributesValue(String key, String value);
}
