package org.openpcm.service;

import java.util.Optional;

import org.openpcm.dao.UserRepository;
import org.openpcm.model.PCMUserDetails;
import org.openpcm.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PCMUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PCMUserDetailsService.class);

    private final UserRepository usersRepository;

    @Autowired
    public PCMUserDetailsService(UserRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUsers = usersRepository.findByUsername(username);

        LOGGER.trace("Checking login attempt for: {}", username);

        optionalUsers.orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        UserDetails details = optionalUsers.map(PCMUserDetails::new).get();

        LOGGER.trace("Found user: {}", details.getUsername());

        return details;
    }
}
