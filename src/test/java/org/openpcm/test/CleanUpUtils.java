package org.openpcm.test;

import java.util.Arrays;
import java.util.List;

import org.openpcm.dao.RoleRepository;
import org.openpcm.dao.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CleanUpUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CleanUpUtils.class);

    public static void clean(final UserRepository userRepository) {
        final List<String> defaultUsers = Arrays.asList("admin");
        userRepository.findAll().forEach(user -> {
            if (!defaultUsers.contains(user.getUsername())) {
                LOGGER.warn("Deleting user: {}", user.getUsername());
                userRepository.deleteById(user.getId());
            }
        });
    }

    public static void clean(final RoleRepository roleRepository) {
        final List<String> defaultRoles = Arrays.asList("ROLE_ADMIN", "ROLE_USER", "ROLE_DOCTOR", "ROLE_NURSE", "ROLE_PATIENT");
        roleRepository.findAll().forEach(role -> {
            if (!defaultRoles.contains(role.getName())) {
                LOGGER.warn("Deleting role: {}", role.getName());
                roleRepository.deleteById(role.getId());
            }
        });
    }
}
