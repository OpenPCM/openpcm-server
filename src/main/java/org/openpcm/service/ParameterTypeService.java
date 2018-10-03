package org.openpcm.service;

import java.util.Optional;

import org.openpcm.dao.ParameterTypeRepository;
import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.ParameterType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ParameterTypeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParameterTypeService.class);

    private final ParameterTypeRepository repository;

    @Autowired
    public ParameterTypeService(ParameterTypeRepository repository) {
        this.repository = repository;
    }

    public ParameterType create(ParameterType parameterType) throws DataViolationException {
        if (!((parameterType.getId() == null) || (parameterType.getId() == 0))) {
            throw new DataViolationException("parameterType id should be null on create");
        }

        LOGGER.trace("Attempting to create parameterType: {}", parameterType);
        return repository.save(parameterType);
    }

    public ParameterType read(Long id) throws NotFoundException {
        final Optional<ParameterType> parameterType = repository.findById(id);

        parameterType.orElseThrow(() -> new NotFoundException(id + " not found"));

        return parameterType.get();
    }

    public Page<ParameterType> read(Pageable pageable) {
        LOGGER.trace("Returning page {} of {} parameterType(s).", pageable.getPageNumber(), pageable.getPageSize());
        return repository.findAll(pageable);
    }

    public ParameterType update(Long id, ParameterType parameterType) throws NotFoundException {
        final Optional<ParameterType> dbParameterType = repository.findById(id);

        dbParameterType.orElseThrow(() -> new NotFoundException(id + " not found"));
        parameterType.setId(dbParameterType.get().getId());
        LOGGER.trace("Attempting to save updated parameterType: {}", parameterType);
        return repository.save(parameterType);
    }

    public void delete(Long id) throws NotFoundException {
        LOGGER.trace("Deleting parameterType: {}", id);

        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new NotFoundException(id + " not found");
        }
    }

}
