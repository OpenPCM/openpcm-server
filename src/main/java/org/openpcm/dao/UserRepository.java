package org.openpcm.dao;

import java.util.List;
import java.util.Optional;

import org.openpcm.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Optional<User> findByMrn(String mrn);

    Optional<User> findBySsn(String ssn);

    Optional<User> findByUsername(String username);

    Page<User> findByRolesNameContaining(String name, Pageable pageable);

    List<User> findByFirstNameContainingAndLastNameContaining(String firstName, String lastName);

    List<User> findByFirstNameAndLastName(String firstName, String lastName);

    @Override
    @EntityGraph(value = "User.attributes", type = EntityGraph.EntityGraphType.LOAD)
    Optional<User> findById(Long id);

}
