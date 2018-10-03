package org.openpcm.dao;

import java.util.Date;
import java.util.Optional;

import org.openpcm.model.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ParameterRepository extends PagingAndSortingRepository<Parameter, Long> {

    Page<Parameter> findByName(String name, Pageable pageable);

    Page<Parameter> findByNameContaining(String name, Pageable pageable);

    Page<Parameter> findByDescriptionContaining(String description, Pageable pageable);

    Page<Parameter> findByUom(String uom, Pageable pageable);

    Page<Parameter> findByUomContaining(String uom, Pageable pageable);

    Page<Parameter> findByNameAndTimestampBetween(String name, Date startTime, Date endTime, Pageable pageable);

    @Override
    @EntityGraph(value = "Parameter.attributes", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Parameter> findById(Long id);

    @Override
    @EntityGraph(value = "Parameter.attributes", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<Parameter> findAll();

    @Override
    @EntityGraph(value = "Parameter.attributes", type = EntityGraph.EntityGraphType.LOAD)
    Page<Parameter> findAll(Pageable pageable);

    @Override
    @EntityGraph(value = "Parameter.attributes", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<Parameter> findAll(Sort sort);

    @Override
    @EntityGraph(value = "Parameter.attributes", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<Parameter> findAllById(Iterable<Long> ids);
}
