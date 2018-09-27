package org.openpcm.service;

import java.util.List;
import java.util.Optional;

import org.openpcm.dao.CollectorRepository;
import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CollectorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectorService.class);

    private final CollectorRepository repository;

    @Autowired
    public CollectorService(CollectorRepository repository) {
        this.repository = repository;
    }

    public Collector create(Collector collector) throws DataViolationException {
        if (!((collector.getId() == null) || (collector.getId() == 0))) {
            throw new DataViolationException("role id should be null on create");
        }

        LOGGER.trace("Attempting to save collector: {}", collector);
        return repository.save(collector);
    }

    public Collector read(Long id) throws NotFoundException {
        final Optional<Collector> role = repository.findById(id);

        LOGGER.trace("Returning collector: {}.", id);

        role.orElseThrow(() -> new NotFoundException(id + " not found"));

        return role.get();
    }

    public Page<Collector> read(Pageable pageable) {
        LOGGER.trace("Returning page {} for {} collector(s).", pageable.getPageNumber(), pageable.getPageSize());
        return repository.findAll(pageable);
    }

    public Collector update(Long id, Collector collector) throws NotFoundException {
        final Optional<Collector> dbCollector = repository.findById(id);
        dbCollector.orElseThrow(() -> new NotFoundException(id + " not found"));
        collector.setId(dbCollector.get().getId());
        LOGGER.trace("Attempting to save collector: {}", collector);
        return repository.save(collector);
    }

    public List<Collector> readByKeyandValue(String key, String value) {
        return repository.findByAttributesKeyAndAttributesValue(key, value);
    }

    public void delete(Long id) {
        LOGGER.trace("Deleting collector: {}", id);

        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }

}
