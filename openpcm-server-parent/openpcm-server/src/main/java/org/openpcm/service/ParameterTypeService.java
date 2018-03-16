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

    private final ParameterTypeRepository parameterTypeRepository;

    @Autowired
    public ParameterTypeService(ParameterTypeRepository parameterTypeRepository) {
        this.parameterTypeRepository = parameterTypeRepository;
    }

    public ParameterType create(ParameterType parameterType) throws DataViolationException {
        if (!(parameterType.getId() == null || parameterType.getId() == 0)) {
            throw new DataViolationException("parameterType id should be null on create");
        }

        LOGGER.trace("Attempting to create parameterType: {}", parameterType);
        return parameterTypeRepository.save(parameterType);
    }

    public ParameterType read(Long id) throws NotFoundException {
        Optional<ParameterType> parameterType = parameterTypeRepository.findById(id);

        if (parameterType.isPresent()) {
            LOGGER.trace("Returning parameterType: {}.", id);
            return parameterType.get();
        } else {
            throw new NotFoundException(id + " not found");
        }
    }

    public Page<ParameterType> read(Pageable pageable) {
        LOGGER.trace("Returning page {} of {} parameterType(s).", pageable.getPageNumber(), pageable.getPageSize());
        return parameterTypeRepository.findAll(pageable);
    }

    public ParameterType update(Long id, ParameterType parameterType) throws NotFoundException {
        Optional<ParameterType> dbParameterType = parameterTypeRepository.findById(id);

        if (!dbParameterType.isPresent()) {
            throw new NotFoundException(id + " not found");
        }

        parameterType.setId(dbParameterType.get().getId());

        LOGGER.trace("Attempting to save updated parameterType: {}", parameterType);

        return parameterTypeRepository.save(parameterType);
    }

    public void delete(Long id) {
        LOGGER.trace("Deleting parameterType: {}", id);

        if (parameterTypeRepository.existsById(id)) {
            parameterTypeRepository.deleteById(id);
        }
    }

}
