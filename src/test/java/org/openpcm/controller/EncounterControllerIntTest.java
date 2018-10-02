package org.openpcm.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openpcm.annotation.IntegrationTest;
import org.openpcm.dao.EncounterRepository;
import org.openpcm.dao.EncounterTypeRepository;
import org.openpcm.dao.ObservationSetRepository;
import org.openpcm.dao.RoleRepository;
import org.openpcm.dao.UserRepository;
import org.openpcm.model.AuthSuccess;
import org.openpcm.model.Encounter;
import org.openpcm.model.EncounterType;
import org.openpcm.model.User;
import org.openpcm.test.CleanUpUtils;
import org.openpcm.test.TestAuthenticationUtils;
import org.openpcm.utils.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Category(IntegrationTest.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@TestInstance(value = Lifecycle.PER_CLASS)
public class EncounterControllerIntTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestAuthenticationUtils authentication;

    @Autowired
    private EncounterRepository repository;

    @Autowired
    private EncounterTypeRepository encounterTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ObservationSetRepository observationSetRepository;

    private AuthSuccess authSuccess;

    private EncounterType dbEncounterType;

    private String base;

    @BeforeAll
    public void beforeAll(@Autowired EncounterType encounterType) {
        dbEncounterType = encounterTypeRepository.save(encounterType);
    }

    @BeforeEach
    public void beforeEach() {
        base = "http://localhost:" + port;
        authSuccess = authentication.retrieveCreds(base);
    }

    @Test
    @DisplayName("Ensure encounter can be created")
    public void test_createSucceeds(@Autowired Encounter encounter) {
        encounter.setTypes(new HashSet<>(Arrays.asList(EncounterType.builder().id(14512L).build())));
        encounter.setPatient(User.builder().id(14511L).build());
        final HttpEntity<String> authHeaders = authentication.convert(ObjectUtil.print(encounter), authSuccess);
        final ResponseEntity<Encounter> result = restTemplate.exchange(base + "/api/v1/encounter", HttpMethod.POST, authHeaders, Encounter.class);

        assertSame(HttpStatus.CREATED, result.getStatusCode(), "incorrect status code");
        final Encounter resultEncounter = result.getBody();
        assertNotNull(resultEncounter.getId(), "property should not be null");
        assertEquals("late", resultEncounter.getAttributes().get(0).getKey(), "incorrect property value");
        assertEquals("yes", resultEncounter.getAttributes().get(0).getValue(), "incorrect property value");
        assertEquals((Long) 14512L, resultEncounter.getTypes().iterator().next().getId(), "incorrect property value");
        assertEquals("CHECKUP", resultEncounter.getTypes().iterator().next().getName(), "incorrect property value");
        assertEquals("demo", resultEncounter.getPatient().getUsername(), "incorrect property value");
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
        encounterTypeRepository.deleteAll();
        CleanUpUtils.clean(userRepository);
        CleanUpUtils.clean(roleRepository);
    }
}
