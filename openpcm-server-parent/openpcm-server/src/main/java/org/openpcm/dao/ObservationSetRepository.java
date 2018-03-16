package org.openpcm.dao;

import java.util.Date;

import org.openpcm.model.ObservationSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ObservationSetRepository extends PagingAndSortingRepository<ObservationSet, Long> {

    Page<ObservationSet> findByOrigin(String origin, Pageable pageable);

    Page<ObservationSet> findByOriginAndTimestampBetween(String origin, Date startTime, Date endTime, Pageable pageable);

    Page<ObservationSet> findByParameters_Name(String name, Pageable pageable);
}
