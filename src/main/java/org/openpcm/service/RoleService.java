package org.openpcm.service;

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
		Optional<Role> role = roleRepository.findById(id);

		LOGGER.trace("Returning role: {}.", id);

		if (role.isPresent()) {
			return role.get();
		} else {
			throw new NotFoundException(id + " not found");
		}
	}

	public Page<Role> read(Pageable pageable) {
		LOGGER.trace("Returning page {} for {} roles.", pageable.getPageNumber(), pageable.getPageSize());
		return roleRepository.findAll(pageable);
	}

	public Role update(Long id, Role role) throws NotFoundException {
		Optional<Role> dbRole = roleRepository.findById(id);

		if (!dbRole.isPresent()) {
			throw new NotFoundException(id + " not found");
		}

		role.setId(dbRole.get().getId());
		LOGGER.trace("Attempting to save role: {}", role);
		return roleRepository.save(role);
	}

	public void delete(Long id) {
		LOGGER.trace("Deleting role: {}", id);

		if (roleRepository.existsById(id)) {
			roleRepository.deleteById(id);
		}
	}
}
