package org.openpcm.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.openpcm.dao.RoleRepository;
import org.openpcm.dao.UserRepository;
import org.openpcm.model.Role;
import org.openpcm.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InitLoaderService implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitLoaderService.class);

    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${openpcm.adminUser:admin}")
    private String adminUsername;

    @Value("${openpcm.adminPassword:openpcm}")
    private String adminPassword;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (alreadySetup)
            return;

        Role adminRole = createRoleIfNotFound("ROLE_ADMIN");
        createRoleIfNotFound("ROLE_USER");

        User adminUser = createAdminUser(adminRole);

        LOGGER.trace("adminUser: {}", adminUser);

    }

    @Transactional
    private Role createRoleIfNotFound(String name) {
        Role result = null;

        Optional<Role> role = roleRepository.findByName(name);
        if (!role.isPresent()) {
            result = Role.builder().name(name).build();
            roleRepository.save(result);

            LOGGER.trace("Creating default role: {}", name);
        } else {
            result = role.get();
        }
        return result;
    }

    @Transactional
    private User createAdminUser(Role adminRole) {
        User result = null;

        Optional<User> role = userRepository.findByUsername(adminUsername);

        if (role.isPresent()) {
            result = role.get();
        } else {
            Set<Role> roles = new HashSet<Role>();
            roles.add(adminRole);
            User adminUser = User.builder().active(true).email(adminUsername + "@openpcm.com").firstName("Admin").lastName("User")
                            .password(passwordEncoder.encode(adminPassword)).username(adminUsername).roles(roles).build();

            result = userRepository.save(adminUser);

            LOGGER.trace("Creating admin user with username: {} and password: {}", adminUsername, adminPassword);
        }

        return result;
    }

}
