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
import org.openpcm.dao.CollectorRepository;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.AuthSuccess;
import org.openpcm.model.Collector;
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
public class CollectorControllerIntTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestAuthenticationUtils authentication;

    @Autowired
    private CollectorRepository repository;

    private AuthSuccess authSuccess;

    private String base;

    @BeforeEach
    public void beforeEach() {
        base = "http://localhost:" + port;
        authSuccess = authentication.retrieveCreds(base);
    }

    @Test
    @DisplayName("Ensure collector can be created")
    public void test_createSucceeds(@Autowired Collector collector) throws URISyntaxException {
        final HttpEntity<String> authHeaders = authentication.convert(ObjectUtil.print(collector), authSuccess);
        final ResponseEntity<Collector> result = restTemplate.exchange(base + "/api/v1/collector", HttpMethod.POST, authHeaders, Collector.class);

        assertSame(HttpStatus.CREATED, result.getStatusCode(), "incorrect status code");
        assertNotNull(result.getBody().getId(), "instance should not be null");
        assertNotNull(result.getBody().getAttributes().get(0), "instance should not be null");
    }

    @Test
    @DisplayName("Ensure collectors can be read")
    public void test_read_pagination_happy(@Autowired Collector collector) throws JsonParseException, JsonMappingException, IOException {
        repository.save(collector);
        final HttpEntity<String> authHeaders = authentication.convert("", authSuccess);
        final ParameterizedTypeReference<RestResponsePage<Collector>> responseType = new ParameterizedTypeReference<RestResponsePage<Collector>>() {
        };
        final ResponseEntity<RestResponsePage<Collector>> result = restTemplate.exchange(base + "/api/v1/collector", HttpMethod.GET, authHeaders, responseType);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertSame(1, result.getBody().getContent().size(), "incorrect number of elements");

    }

    @Test
    @DisplayName("Ensure collector can be read by id")
    public void test_read_byId_happy(@Autowired Collector collector) throws NotFoundException {
        repository.save(collector);
        final HttpEntity<String> authHeaders = authentication.convert("", authSuccess);
        final ResponseEntity<Collector> result = restTemplate.exchange(base + "/api/v1/collector/" + collector.getId(), HttpMethod.GET, authHeaders,
                        Collector.class);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertEquals(collector.getId(), result.getBody().getId(), "incorrect property value");
        assertEquals(collector.getAttributes().get(0), result.getBody().getAttributes().get(0), "incorrect property value");
    }

    @Test
    @DisplayName("Ensure collector can be updated")
    public void test_update_happy(@Autowired Collector collector) throws NotFoundException {
        repository.save(collector);
        collector.setName("New Name");
        final HttpEntity<String> authHeaders = authentication.convert(ObjectUtil.print(collector), authSuccess);
        final ResponseEntity<Collector> result = restTemplate.exchange(base + "/api/v1/collector/" + collector.getId(), HttpMethod.PUT, authHeaders,
                        Collector.class);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertEquals(collector.getId(), result.getBody().getId(), "incorrect property value");
        assertEquals("New Name", result.getBody().getName(), "incorrect property value");
    }

    @Test
    @DisplayName("Ensure collector can be deleted")
    public void test_delete_happy(@Autowired Collector collector) throws NotFoundException {
        repository.save(collector);
        final HttpEntity<String> authHeaders = authentication.convert("", authSuccess);
        final ResponseEntity<String> result = restTemplate.exchange(base + "/api/v1/collector/" + collector.getId(), HttpMethod.DELETE, authHeaders,
                        String.class);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertFalse(repository.existsById(collector.getId()), "id should not exist");
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
    }
}
