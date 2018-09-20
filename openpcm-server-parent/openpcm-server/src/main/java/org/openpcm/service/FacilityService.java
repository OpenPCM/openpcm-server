package org.openpcm.service;

import org.openpcm.dao.FacilityRepository;
import org.openpcm.dao.RoomRepository;
import org.openpcm.exceptions.DataViolationException;
import org.openpcm.model.Facility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class FacilityService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FacilityService.class);

	private final FacilityRepository facilityRepository;

	private final RoomRepository roomRepository;

	@Autowired
	public FacilityService(FacilityRepository facilityRepository, RoomRepository roomRepository) {
		this.facilityRepository = facilityRepository;
		this.roomRepository = roomRepository;
	}

	public Facility create(Facility facility) throws DataViolationException {
		if (!(facility.getId() == null || facility.getId() == 0)) {
			throw new DataViolationException("encounter id should be null on create");
		}

		LOGGER.trace("Attempting to save facility: {}", facility);
		return facilityRepository.save(facility);
	}
}
