package org.openpcm.service;

import java.util.Optional;

import org.openpcm.dao.ObservationSetRepository;
import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.ObservationSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ObservationSetService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ObservationSetService.class);

	private final ObservationSetRepository repository;

	@Autowired
	public ObservationSetService(ObservationSetRepository repository) {
		this.repository = repository;
	}

	public ObservationSet create(ObservationSet observationSet) throws DataViolationException {
		if (!(observationSet.getId() == null || observationSet.getId() == 0)) {
			throw new DataViolationException("parameterType id should be null on create");
		}

		LOGGER.trace("Attempting to create observationSet: {}", observationSet);
		return repository.save(observationSet);
	}

	public ObservationSet read(Long id) throws NotFoundException {
		Optional<ObservationSet> observationSet = repository.findById(id);

		if (observationSet.isPresent()) {
			LOGGER.trace("Returning observationSet: {}.", id);
			return observationSet.get();
		} else {
			throw new NotFoundException(id + " not found");
		}
	}

	public Page<ObservationSet> read(Pageable pageable) {
		LOGGER.trace("Returning page {} of {} observationSet(s).", pageable.getPageNumber(), pageable.getPageSize());
		return repository.findAll(pageable);
	}

	public ObservationSet update(Long id, ObservationSet observationSet) throws NotFoundException {
		Optional<ObservationSet> dbObservationSet = repository.findById(id);

		if (!dbObservationSet.isPresent()) {
			throw new NotFoundException(id + " not found");
		}

		observationSet.setId(dbObservationSet.get().getId());

		LOGGER.trace("Attempting to save updated ObservationSet: {}", observationSet);

		return repository.save(observationSet);
	}

	public void delete(Long id) {
		LOGGER.trace("Deleting observationSet: {}", id);

		if (repository.existsById(id)) {
			repository.deleteById(id);
		}
	}
}
