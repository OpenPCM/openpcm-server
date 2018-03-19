package org.openpcm.service;

import java.util.Optional;

import org.openpcm.dao.EncounterRepository;
import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.Encounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class EncounterService {
	private static final Logger LOGGER = LoggerFactory.getLogger(EncounterService.class);

	private final EncounterRepository repository;

	@Autowired
	public EncounterService(EncounterRepository repository) {
		this.repository = repository;
	}

	public Encounter create(Encounter encounter) throws DataViolationException {
		if (!(encounter.getId() == null || encounter.getId() == 0)) {
			throw new DataViolationException("encounter id should be null on create");
		}

		LOGGER.trace("Attempting to save encounter: {}", encounter);
		return repository.save(encounter);
	}

	public Encounter read(Long id) throws NotFoundException {
		Optional<Encounter> encounter = repository.findById(id);

		LOGGER.trace("Returning encounter: {}.", id);

		if (encounter.isPresent()) {
			return encounter.get();
		} else {
			throw new NotFoundException(id + " not found");
		}
	}

	public Page<Encounter> read(Pageable pageable) {
		LOGGER.trace("Returning page {} for {} encounter(s).", pageable.getPageNumber(), pageable.getPageSize());
		return repository.findAll(pageable);
	}

	public Encounter update(Long id, Encounter encounter) throws NotFoundException {
		Optional<Encounter> dbEncounter = repository.findById(id);

		if (!dbEncounter.isPresent()) {
			throw new NotFoundException(id + " not found");
		}

		encounter.setId(dbEncounter.get().getId());
		LOGGER.trace("Attempting to save encounter: {}", encounter);
		return repository.save(encounter);
	}

	public void delete(Long id) {
		LOGGER.trace("Deleting encounter: {}", id);

		if (repository.existsById(id)) {
			repository.deleteById(id);
		}
	}
}
