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
import org.openpcm.dao.ObservationSetRepository;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.AuthSuccess;
import org.openpcm.model.ObservationSet;
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
public class ObservationSetControllerIntTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestAuthenticationUtils authentication;

    @Autowired
    private ObservationSetRepository repository;

    private AuthSuccess authSuccess;

    private String base;

    @BeforeEach
    public void beforeEach() {
        base = "http://localhost:" + port;
        authSuccess = authentication.retrieveCreds(base);
    }

    @Test
    @DisplayName("Ensure observation set can be created")
    public void test_createSucceeds(@Autowired ObservationSet set) throws URISyntaxException {
        final HttpEntity<String> authHeaders = authentication.convert(ObjectUtil.print(set), authSuccess);
        final ResponseEntity<ObservationSet> result = restTemplate.exchange(base + "/api/v1/observationset", HttpMethod.POST, authHeaders,
                        ObservationSet.class);

        assertSame(HttpStatus.CREATED, result.getStatusCode(), "incorrect status code");
        assertNotNull(result.getBody().getId(), "instance should not be null");
        assertNotNull(result.getBody().getParameters().get(0).getId(), "instance should not be null");
    }

    @Test
    public void test_read_pagination_happy(@Autowired ObservationSet set) throws JsonParseException, JsonMappingException, IOException {
        repository.save(set);
        final HttpEntity<String> authHeaders = authentication.convert("", authSuccess);
        final ParameterizedTypeReference<RestResponsePage<ObservationSet>> responseType = new ParameterizedTypeReference<RestResponsePage<ObservationSet>>() {
        };
        final ResponseEntity<RestResponsePage<ObservationSet>> result = restTemplate.exchange(base + "/api/v1/observationset", HttpMethod.GET, authHeaders,
                        responseType);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertSame(1, result.getBody().getContent().size(), "incorrect number of elements");
        assertSame(1, result.getBody().getContent().size(), "incorrect number of elements");

    }

    @Test
    public void test_read_byId_happy(@Autowired ObservationSet set) throws NotFoundException {
        repository.save(set);
        final HttpEntity<String> authHeaders = authentication.convert("", authSuccess);
        final ResponseEntity<ObservationSet> result = restTemplate.exchange(base + "/api/v1/observationset/" + set.getId(), HttpMethod.GET, authHeaders,
                        ObservationSet.class);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertEquals(set.getId(), result.getBody().getId(), "incorrect property value");
        assertEquals(set.getParameters().get(0).getId(), result.getBody().getParameters().get(0).getId(), "incorrect property value");
    }

    @Test
    public void test_update_happy(@Autowired ObservationSet set) throws NotFoundException {
        repository.save(set);
        set.setOrigin("Device5");
        final HttpEntity<String> authHeaders = authentication.convert(ObjectUtil.print(set), authSuccess);
        final ResponseEntity<ObservationSet> result = restTemplate.exchange(base + "/api/v1/observationset/" + set.getId(), HttpMethod.PUT, authHeaders,
                        ObservationSet.class);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertEquals(set.getId(), result.getBody().getId(), "incorrect property value");
        assertEquals("Device5", result.getBody().getOrigin(), "incorrect property value");
    }

    @Test
    public void test_delete_happy(@Autowired ObservationSet set) throws NotFoundException {
        repository.save(set);
        final HttpEntity<String> authHeaders = authentication.convert("", authSuccess);
        final ResponseEntity<String> result = restTemplate.exchange(base + "/api/v1/observationset/" + set.getId(), HttpMethod.DELETE, authHeaders,
                        String.class);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertFalse(repository.existsById(set.getId()), "id should not exist");
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
    }

}
