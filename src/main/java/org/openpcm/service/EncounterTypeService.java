package org.openpcm.service;

import java.util.Optional;

import org.openpcm.dao.EncounterTypeRepository;
import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.EncounterType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class EncounterTypeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncounterTypeService.class);

    private final EncounterTypeRepository repository;

    @Autowired
    public EncounterTypeService(EncounterTypeRepository repository) {
        this.repository = repository;
    }

    public EncounterType create(EncounterType encounterType) throws DataViolationException {
        if (!((encounterType.getId() == null) || (encounterType.getId() == 0))) {
            throw new DataViolationException("enounterType id should be null on create");
        }

        LOGGER.trace("Attempting to save encounterType: {}", encounterType);
        return repository.save(encounterType);
    }

    public EncounterType read(Long id) throws NotFoundException {
        final Optional<EncounterType> encounterType = repository.findById(id);
        encounterType.orElseThrow(() -> new NotFoundException(id + " not found"));
        LOGGER.trace("Returning encounterType: {}.", encounterType);
        return encounterType.get();
    }

    public Page<EncounterType> read(Pageable pageable) {
        LOGGER.trace("Returning page {} for {} encounterType(s).", pageable.getPageNumber(), pageable.getPageSize());
        return repository.findAll(pageable);
    }

    public EncounterType update(Long id, EncounterType encounterType) throws NotFoundException {
        final Optional<EncounterType> dbEncounterType = repository.findById(id);
        dbEncounterType.orElseThrow(() -> new NotFoundException(id + " not found"));
        encounterType.setId(dbEncounterType.get().getId());
        LOGGER.trace("Attempting to save encounterType: {}", encounterType);
        return repository.save(encounterType);
    }

    public void delete(Long id) {
        LOGGER.trace("Deleting encounterType: {}", id);

        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }
}
