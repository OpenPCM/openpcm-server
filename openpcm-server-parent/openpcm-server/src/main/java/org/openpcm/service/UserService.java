package org.openpcm.service;

import java.util.Optional;

import org.openpcm.dao.UserRepository;
import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Transactional
@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(User user) throws DataViolationException {
        if (!(user.getId() == null || user.getId() == 0)) {
            throw new DataViolationException("user id should be null on create");
        }

        if (user.getAddress() == null && (user.getAddress().getId() != null && user.getAddress().getId() != 0)) {
            throw new DataViolationException("address id should be null on create");
        }

        if (!StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        LOGGER.trace("Attempting to create user: {}", user);

        return userRepository.save(user);
    }

    public User read(Long id) throws NotFoundException {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            LOGGER.trace("Returning user: {}.", id);
            return user.get();
        } else {
            throw new NotFoundException(id + " not found");
        }
    }

    public Page<User> read(Pageable pageable) {
        LOGGER.trace("Returning page {} of {} user(s).", pageable.getPageNumber(), pageable.getPageSize());
        return userRepository.findAll(pageable);
    }

    public User update(Long id, User user) throws NotFoundException {
        Optional<User> dbUser = userRepository.findById(id);

        if (!dbUser.isPresent()) {
            throw new NotFoundException(id + " not found");
        }

        // detecting if password changed, if it did we encrypt it because it's raw password
        if (!StringUtils.isEmpty(user.getPassword())) {
            if (!user.getPassword().equals(dbUser.get().getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }

        user.setId(dbUser.get().getId());

        LOGGER.trace("Attempting to save updated user: {}", user);

        return userRepository.save(user);
    }

    public void delete(Long id) {
        LOGGER.trace("Deleting user: {}", id);

        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
    }
}
