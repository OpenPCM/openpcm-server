package org.openpcm.service;

import java.util.List;
import java.util.Optional;

import org.openpcm.dao.RoleRepository;
import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Transactional
@Service
public class RoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleService.class);

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role create(Role role) throws DataViolationException {
        if (!(role.getId() == null || role.getId() == 0)) {
            throw new DataViolationException("role id should be null on create");
        }

        LOGGER.trace("Attempting to save role: {}", role);
        return roleRepository.save(role);
    }

    public Role read(Long id) throws NotFoundException {
        final Optional<Role> role = roleRepository.findById(id);

        role.orElseThrow(() -> new NotFoundException("could not be found by id: " + id));
        LOGGER.trace("Returning role: {}.", role);
        return role.get();
    }

    public Page<Role> read(Pageable pageable) {
        LOGGER.trace("Returning page {} for {} roles.", pageable.getPageNumber(), pageable.getPageSize());
        return roleRepository.findAll(pageable);
    }

    public List<Role> readAll() {
        LOGGER.trace("Returning all roles.");
        return Lists.newArrayList(roleRepository.findAll());
    }

    public Role update(Long id, Role role) throws NotFoundException {
        final Optional<Role> dbRole = roleRepository.findById(id);
        dbRole.orElseThrow(() -> new NotFoundException(id + " not found"));
        role.setId(dbRole.get().getId());
        LOGGER.trace("Attempting to save role: {}", role);
        return roleRepository.save(role);
    }

    public void delete(Long id) {
        LOGGER.trace("Deleting role: {}", id);

        if (roleRepository.existsById(id)) {
            roleRepository.deleteById(id);
        } else {
            throw new NotFoundException("could not be found by id: " + id);
        }
    }
}
