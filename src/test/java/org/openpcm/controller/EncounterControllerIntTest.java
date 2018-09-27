package org.openpcm.controller;

import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openpcm.annotation.IntegrationTest;
import org.openpcm.dao.EncounterRepository;
import org.openpcm.model.AuthSuccess;
import org.openpcm.test.TestAuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Category(IntegrationTest.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class EncounterControllerIntTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestAuthenticationUtils authentication;

    @Autowired
    private EncounterRepository repository;

    private AuthSuccess authSuccess;

    private String base;

    @BeforeEach
    public void beforeEach() {
        base = "http://localhost:" + port;
        authSuccess = authentication.retrieveCreds(base);
    }
}
