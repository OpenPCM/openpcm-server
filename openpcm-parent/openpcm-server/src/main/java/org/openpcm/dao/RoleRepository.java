package org.openpcm.dao;

import java.util.Optional;

import org.openpcm.model.Role;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {

    Optional<Role> findByName(String name);
}
