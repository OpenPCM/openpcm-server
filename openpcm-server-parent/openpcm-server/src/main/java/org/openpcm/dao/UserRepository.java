package org.openpcm.dao;

import java.util.List;
import java.util.Optional;

import org.openpcm.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Optional<User> findBySsn(String ssn);

    Optional<User> findByUsername(String username);

    List<User> findByFirstNameContainingAndLastNameContaining(String firstName, String lastName);

    Page<User> findAll(Pageable pageable);

    List<User> findByFirstNameAndLastName(String firstName, String lastName);

}
