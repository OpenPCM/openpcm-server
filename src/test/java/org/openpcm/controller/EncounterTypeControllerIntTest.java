package org.openpcm.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openpcm.annotation.IntegrationTest;
import org.openpcm.dao.EncounterTypeRepository;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.AuthSuccess;
import org.openpcm.model.EncounterType;
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
public class EncounterTypeControllerIntTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestAuthenticationUtils authentication;

    @Autowired
    private EncounterTypeRepository repository;

    private AuthSuccess authSuccess;

    private String base;

    @BeforeEach
    public void beforeEach() {
        base = "http://localhost:" + port;
        authSuccess = authentication.retrieveCreds(base);
    }

    @Test
    @DisplayName("Ensure observation set can be created")
    public void test_createSucceeds(@Autowired EncounterType encounterType) throws URISyntaxException {
        final HttpEntity<String> authHeaders = authentication.convert(ObjectUtil.print(encounterType), authSuccess);
        final ResponseEntity<EncounterType> result = restTemplate.exchange(base + "/api/v1/encountertype", HttpMethod.POST, authHeaders, EncounterType.class);

        assertSame(HttpStatus.CREATED, result.getStatusCode(), "incorrect status code");
        assertNotNull(result.getBody().getId(), "instance should not be null");
        assertEquals("DOCTOR-VISIT", result.getBody().getName(), "property value is incorrect");
    }

    @Test
    public void test_read_pagination_happy(@Autowired EncounterType encounterType) throws JsonParseException, JsonMappingException, IOException {
        repository.save(encounterType);
        final HttpEntity<String> authHeaders = authentication.convert("", authSuccess);
        final ParameterizedTypeReference<RestResponsePage<EncounterType>> responseType = new ParameterizedTypeReference<RestResponsePage<EncounterType>>() {
        };
        final ResponseEntity<RestResponsePage<EncounterType>> result = restTemplate.exchange(base + "/api/v1/encountertype", HttpMethod.GET, authHeaders,
                        responseType);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertSame(1, result.getBody().getContent().size(), "incorrect number of elements");
        assertEquals("DOCTOR-VISIT", result.getBody().getContent().get(0).getName(), "property value is incorrect");

    }

    @Test
    public void test_read_byId_happy(@Autowired EncounterType encounterType) throws NotFoundException {
        repository.save(encounterType);
        final HttpEntity<String> authHeaders = authentication.convert("", authSuccess);
        final ResponseEntity<EncounterType> result = restTemplate.exchange(base + "/api/v1/encountertype/" + encounterType.getId(), HttpMethod.GET, authHeaders,
                        EncounterType.class);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertEquals(encounterType.getId(), result.getBody().getId(), "incorrect property value");
        assertEquals(encounterType.getName(), result.getBody().getName(), "incorrect property value");
    }

    @Test
    public void test_update_happy(@Autowired EncounterType encounterType) throws NotFoundException {
        repository.save(encounterType);
        encounterType.setName("NURSE-CALL");
        final HttpEntity<String> authHeaders = authentication.convert(ObjectUtil.print(encounterType), authSuccess);
        final ResponseEntity<EncounterType> result = restTemplate.exchange(base + "/api/v1/encountertype/" + encounterType.getId(), HttpMethod.PUT, authHeaders,
                        EncounterType.class);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertEquals(encounterType.getId(), result.getBody().getId(), "incorrect property value");
        assertEquals("NURSE-CALL", result.getBody().getName(), "incorrect property value");
    }

    @Test
    public void test_delete_happy(@Autowired EncounterType encounterType) throws NotFoundException {
        repository.save(encounterType);
        final HttpEntity<String> authHeaders = authentication.convert("", authSuccess);
        final ResponseEntity<String> result = restTemplate.exchange(base + "/api/v1/encountertype/" + encounterType.getId(), HttpMethod.DELETE, authHeaders,
                        String.class);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertFalse(repository.existsById(encounterType.getId()), "id should not exist");
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
    }
}
