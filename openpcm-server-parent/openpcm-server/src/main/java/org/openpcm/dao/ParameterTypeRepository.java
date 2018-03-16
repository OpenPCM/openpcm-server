package org.openpcm.dao;

import org.openpcm.model.ParameterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ParameterTypeRepository extends PagingAndSortingRepository<ParameterType, Long> {

    Page<ParameterType> findByName(String name, Pageable pageable);

    Page<ParameterType> findByNameContaining(String name, Pageable pageable);

    Page<ParameterType> findByDescriptionContaining(String description, Pageable pageable);

    Page<ParameterType> findByUom(String uom, Pageable pageable);

    Page<ParameterType> findByUomContaining(String uom, Pageable pageable);
}
