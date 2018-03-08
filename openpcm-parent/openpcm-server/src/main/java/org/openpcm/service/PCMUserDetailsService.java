package org.openpcm.service;

import java.util.Optional;

import org.openpcm.dao.UserRepository;
import org.openpcm.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PCMUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PCMUserDetailsService.class);

    // private final UserRepository userRepository;
    // private final AuthenticationManager authenticationManager;
    // private final PasswordEncoder passwordEncoder;
    //
    // @Autowired
    // public PCMUserDetailsService(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
    // this.userRepository = userRepository;
    // this.authenticationManager = authenticationManager;
    // this.passwordEncoder = passwordEncoder;
    // }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        LOGGER.trace("Checking login attempt for: {}", username);

        optionalUser.orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));

        LOGGER.trace("Found user: {}", optionalUser.get().getUsername());

        return optionalUser.get();
    }

    public void changePassword(String oldPassword, String newPassword) {

        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        String username = currentUser.getName();
        LOGGER.debug("Password change request for: {}", username);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));

        User user = (User) loadUserByUsername(username);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        LOGGER.debug("Successfully changed password for: {}", username);

    }
}
