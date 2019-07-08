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
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openpcm.annotation.IntegrationTest;
import org.openpcm.dao.ParameterTypeRepository;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.AuthSuccess;
import org.openpcm.model.ParameterType;
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
public class ParameterTypeControllerIntTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestAuthenticationUtils authentication;

    @Autowired
    private ParameterTypeRepository repository;

    private AuthSuccess authSuccess;

    private String base;

    @BeforeEach
    public void beforeEach() {
        base = "http://localhost:" + port;
        authSuccess = authentication.retrieveCreds(base);
    }

    @Test
    @DisplayName("Ensure parameter type can be created")
    public void test_createSucceeds(@Autowired ParameterType parameterType) throws URISyntaxException {
        final HttpEntity<String> authHeaders = authentication.convert(ObjectUtil.print(parameterType), authSuccess);
        final ResponseEntity<ParameterType> result = restTemplate.exchange(base + "/api/v1/parametertype", HttpMethod.POST, authHeaders, ParameterType.class);

        assertSame(HttpStatus.CREATED, result.getStatusCode(), "incorrect status code");
        assertNotNull(result.getBody().getId(), "instance should not be null");
        assertEquals("HR", result.getBody().getName(), "property value is incorrect");
        assertEquals("Heart Rate", result.getBody().getDescription(), "property value is incorrect");
        assertEquals("bpm", result.getBody().getUom(), "property value is incorrect");
    }

    @Test
    @DisplayName("Ensure parameter type can be read")
    public void test_read_pagination_happy(@Autowired ParameterType parameterType) throws JsonParseException, JsonMappingException, IOException {
        repository.save(parameterType);
        final HttpEntity<String> authHeaders = authentication.convert("", authSuccess);
        final ParameterizedTypeReference<RestResponsePage<ParameterType>> responseType = new ParameterizedTypeReference<RestResponsePage<ParameterType>>() {
        };
        final ResponseEntity<RestResponsePage<ParameterType>> result = restTemplate.exchange(base + "/api/v1/parametertype", HttpMethod.GET, authHeaders,
                        responseType);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertSame(1, result.getBody().getContent().size(), "incorrect number of elements");
        assertEquals("HR", result.getBody().getContent().get(0).getName(), "property value is incorrect");
        assertEquals("Heart Rate", result.getBody().getContent().get(0).getDescription(), "property value is incorrect");
        assertEquals("bpm", result.getBody().getContent().get(0).getUom(), "property value is incorrect");
    }

    @Test
    @DisplayName("Ensure parameter type can be read by id")
    public void test_read_byId_happy(@Autowired ParameterType parameterType) throws NotFoundException {
        repository.save(parameterType);
        final HttpEntity<String> authHeaders = authentication.convert("", authSuccess);
        final ResponseEntity<ParameterType> result = restTemplate.exchange(base + "/api/v1/parametertype/" + parameterType.getId(), HttpMethod.GET, authHeaders,
                        ParameterType.class);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertEquals(parameterType.getId(), result.getBody().getId(), "incorrect property value");
        assertEquals(parameterType.getName(), result.getBody().getName(), "incorrect property value");
        assertEquals(parameterType.getDescription(), result.getBody().getDescription(), "property value is incorrect");
        assertEquals(parameterType.getUom(), result.getBody().getUom(), "property value is incorrect");
    }

    @Test
    @DisplayName("Ensure parameter type can be updated")
    public void test_update_happy(@Autowired ParameterType parameterType) throws NotFoundException {
        repository.save(parameterType);
        parameterType.setName("PR");
        parameterType.setDescription("Pulse Rate");
        parameterType.setUom("beats per minute");
        final HttpEntity<String> authHeaders = authentication.convert(ObjectUtil.print(parameterType), authSuccess);
        final ResponseEntity<ParameterType> result = restTemplate.exchange(base + "/api/v1/parametertype/" + parameterType.getId(), HttpMethod.PUT, authHeaders,
                        ParameterType.class);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertEquals(parameterType.getId(), result.getBody().getId(), "incorrect property value");
        assertEquals("PR", result.getBody().getName(), "incorrect property value");
        assertEquals("Pulse Rate", result.getBody().getDescription(), "incorrect property value");
        assertEquals("beats per minute", result.getBody().getUom(), "incorrect property value");
    }

    @Test
    @DisplayName("Ensure parameter type can be deleted")
    public void test_delete_happy(@Autowired ParameterType parameterType) throws NotFoundException {
        repository.save(parameterType);
        final HttpEntity<String> authHeaders = authentication.convert("", authSuccess);
        final ResponseEntity<String> result = restTemplate.exchange(base + "/api/v1/parametertype/" + parameterType.getId(), HttpMethod.DELETE, authHeaders,
                        String.class);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertFalse(repository.existsById(parameterType.getId()), "id should not exist");
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
    }
}
