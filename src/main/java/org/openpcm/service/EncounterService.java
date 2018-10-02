package org.openpcm.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.openpcm.dao.EncounterRepository;
import org.openpcm.dao.EncounterTypeRepository;
import org.openpcm.dao.UserRepository;
import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.DomainInvalidException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.Encounter;
import org.openpcm.model.EncounterType;
import org.openpcm.model.User;
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
    private final EncounterTypeRepository encounterTypeRepository;
    private final UserRepository userRepository;

    @Autowired
    public EncounterService(EncounterRepository repository, EncounterTypeRepository encounterTypeRepository, UserRepository userRepository) {
        this.repository = repository;
        this.encounterTypeRepository = encounterTypeRepository;
        this.userRepository = userRepository;
    }

    public Encounter create(Encounter encounter) throws DataViolationException {
        validate(encounter);

        LOGGER.trace("Attempting to save encounter: {}", encounter);
        return repository.save(encounter);
    }

    public Encounter read(Long id) throws NotFoundException {
        final Optional<Encounter> encounter = repository.findById(id);

        encounter.orElseThrow(() -> new NotFoundException(id + " not found"));
        LOGGER.trace("Returning encounter: {}.", encounter);
        return encounter.get();
    }

    public Page<Encounter> read(Pageable pageable) {
        LOGGER.trace("Returning page {} for {} encounter(s).", pageable.getPageNumber(), pageable.getPageSize());
        return repository.findAll(pageable);
    }

    public Encounter update(Long id, Encounter encounter) throws NotFoundException {
        final Optional<Encounter> dbEncounter = repository.findById(id);
        dbEncounter.orElseThrow(() -> new NotFoundException(id + " not found"));
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

    private void validate(Encounter encounter) {
        if (!(encounter.getId() == null || encounter.getId() == 0)) {
            throw new DomainInvalidException("encounter id should be null on create");
        }

        final Set<EncounterType> encounterTypes = encounter.getTypes();
        final Set<EncounterType> dbEncounterTypes = new HashSet<EncounterType>();
        Optional<EncounterType> current;
        for (final EncounterType encounterType : encounterTypes) {
            current = encounterTypeRepository.findById(encounterType.getId());
            if (current.isPresent()) {
                dbEncounterTypes.add(current.get());
            } else {
                throw new DomainInvalidException("EncounterType: " + encounterType.getId() + " not found");
            }
        }

        encounter.setTypes(dbEncounterTypes);

        final Optional<User> user = encounter.getPatient() != null ? userRepository.findById(encounter.getPatient().getId()) : Optional.empty();
        if (user.isPresent()) {
            encounter.setPatient(user.get());
        } else {
            throw new DomainInvalidException("User could not be found");
        }
    }
}
