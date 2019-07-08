package org.openpcm.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.IOException;

import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openpcm.annotation.IntegrationTest;
import org.openpcm.dao.EncounterRepository;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.AuthSuccess;
import org.openpcm.model.Encounter;
import org.openpcm.test.RestResponsePage;
import org.openpcm.test.TestAuthenticationUtils;
import org.openpcm.utils.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Category(IntegrationTest.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Execution(ExecutionMode.SAME_THREAD)
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

    @Test
    @DisplayName("Ensure encounter can be created")
    public void test_createSucceeds(@Autowired Encounter encounter) {
        final HttpEntity<String> authHeaders = authentication.convert(ObjectUtil.print(encounter), authSuccess);
        final ResponseEntity<Encounter> result = restTemplate.exchange(base + "/api/v1/encounter", HttpMethod.POST, authHeaders, Encounter.class);

        assertSame(HttpStatus.CREATED, result.getStatusCode(), "incorrect status code");
        final Encounter resultEncounter = result.getBody();
        assertNotNull(resultEncounter.getId(), "property should not be null");
        assertEquals("late", resultEncounter.getAttributes().iterator().next().getKey(), "incorrect property value");
        assertEquals("yes", resultEncounter.getAttributes().iterator().next().getValue(), "incorrect property value");
        assertEquals((Long) 14512L, resultEncounter.getTypes().iterator().next().getId(), "incorrect property value");
        assertEquals("CHECKUP", resultEncounter.getTypes().iterator().next().getName(), "incorrect property value");
        assertEquals("demo", resultEncounter.getPatient().getUsername(), "incorrect property value");
        assertEquals("First Checkup", resultEncounter.getTitle(), "incorrect property value");
        assertEquals("Checkup on patient", resultEncounter.getDescription(), "incorrect property value");
        assertNotNull(resultEncounter.getTimestamp(), "incorrect property value");
    }

    @Test
    @DisplayName("Ensure encounter can be read")
    public void test_read_pagination_happy(@Autowired Encounter encounter) throws JsonParseException, JsonMappingException, IOException {
        repository.save(encounter);
        final HttpEntity<String> authHeaders = authentication.convert("", authSuccess);
        final ParameterizedTypeReference<RestResponsePage<Encounter>> responseType = new ParameterizedTypeReference<RestResponsePage<Encounter>>() {
        };
        final ResponseEntity<RestResponsePage<Encounter>> result = restTemplate.exchange(base + "/api/v1/encounter", HttpMethod.GET, authHeaders, responseType);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertSame(1, result.getBody().getContent().size(), "incorrect number of elements");
        final Encounter returnEncounter = result.getBody().getContent().get(0);
        assertNotNull(returnEncounter.getId(), "property should not be null");
        assertEquals("late", returnEncounter.getAttributes().iterator().next().getKey(), "incorrect property value");
        assertEquals("yes", returnEncounter.getAttributes().iterator().next().getValue(), "incorrect property value");
        assertEquals((Long) 14512L, returnEncounter.getTypes().iterator().next().getId(), "incorrect property value");
        assertEquals("CHECKUP", returnEncounter.getTypes().iterator().next().getName(), "incorrect property value");
        assertEquals("demo", returnEncounter.getPatient().getUsername(), "incorrect property value");
        assertEquals("First Checkup", returnEncounter.getTitle(), "incorrect property value");
        assertEquals("Checkup on patient", returnEncounter.getDescription(), "incorrect property value");
        assertNotNull(returnEncounter.getTimestamp(), "incorrect property value");
    }

    @Test
    @DisplayName("Ensure encounter can be read by id")
    public void test_read_byId_happy(@Autowired Encounter encounter) throws JsonParseException, JsonMappingException, IOException {
        repository.save(encounter);
        final HttpEntity<String> authHeaders = authentication.convert("", authSuccess);
        final ResponseEntity<Encounter> result = restTemplate.exchange(base + "/api/v1/encounter/" + encounter.getId(), HttpMethod.GET, authHeaders,
                        Encounter.class);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        final Encounter returnEncounter = result.getBody();
        assertNotNull(returnEncounter.getId(), "property should not be null");
        assertEquals("late", returnEncounter.getAttributes().iterator().next().getKey(), "incorrect property value");
        assertEquals("yes", returnEncounter.getAttributes().iterator().next().getValue(), "incorrect property value");
        assertEquals((Long) 14512L, returnEncounter.getTypes().iterator().next().getId(), "incorrect property value");
        assertEquals("CHECKUP", returnEncounter.getTypes().iterator().next().getName(), "incorrect property value");
        assertEquals("demo", returnEncounter.getPatient().getUsername(), "incorrect property value");
        assertEquals("First Checkup", returnEncounter.getTitle(), "incorrect property value");
        assertEquals("Checkup on patient", returnEncounter.getDescription(), "incorrect property value");
        assertNotNull(returnEncounter.getTimestamp(), "incorrect property value");
    }

    @Test
    @DisplayName("Ensure encounter can be updated")
    public void test_update_happy(@Autowired Encounter encounter) throws NotFoundException {
        repository.save(encounter);
        encounter.setTitle("No Problems");
        final HttpEntity<String> authHeaders = authentication.convert(ObjectUtil.print(encounter), authSuccess);
        final ResponseEntity<Encounter> result = restTemplate.exchange(base + "/api/v1/encounter/" + encounter.getId(), HttpMethod.PUT, authHeaders,
                        Encounter.class);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertEquals(encounter.getId(), result.getBody().getId(), "incorrect property value");
        assertEquals("No Problems", result.getBody().getTitle(), "incorrect property value");
    }

    @Test
    @DisplayName("Ensure encounter can be deleted")
    public void test_delete_happy(@Autowired Encounter encounter) throws NotFoundException {
        repository.save(encounter);
        final HttpEntity<String> authHeaders = authentication.convert("", authSuccess);
        final ResponseEntity<String> result = restTemplate.exchange(base + "/api/v1/encounter/" + encounter.getId(), HttpMethod.DELETE, authHeaders,
                        String.class);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertFalse(repository.existsById(encounter.getId()), "id should not exist");
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
    }
}
