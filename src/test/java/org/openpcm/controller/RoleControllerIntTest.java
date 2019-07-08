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
import org.openpcm.dao.RoleRepository;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.AuthSuccess;
import org.openpcm.model.Role;
import org.openpcm.test.CleanUpUtils;
import org.openpcm.test.RestResponsePage;
import org.openpcm.test.TestAuthenticationUtils;
import org.openpcm.utils.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class RoleControllerIntTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleControllerIntTest.class);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestAuthenticationUtils authentication;

    @Autowired
    private RoleRepository repository;

    private AuthSuccess authSuccess;

    private String base;

    @BeforeEach
    public void beforeEach() {
        base = "http://localhost:" + port;
        authSuccess = authentication.retrieveCreds(base);
    }

    @Test
    @DisplayName("Ensure role can be created")
    public void test_createSucceeds(@Autowired Role role) throws URISyntaxException {
        final HttpEntity<String> authHeaders = authentication.convert(ObjectUtil.print(role), authSuccess);
        final ResponseEntity<Role> result = restTemplate.exchange(base + "/api/v1/role", HttpMethod.POST, authHeaders, Role.class);

        assertSame(HttpStatus.CREATED, result.getStatusCode(), "incorrect status code");
        assertNotNull(result.getBody().getId(), "instance should not be null");
        assertEquals("TESTER", result.getBody().getName(), "property value is incorrect");
    }

    @Test
    @DisplayName("Ensure role can be read")
    public void test_read_pagination_happy(@Autowired Role role) throws JsonParseException, JsonMappingException, IOException {
        repository.save(role);
        final HttpEntity<String> authHeaders = authentication.convert("", authSuccess);
        final ParameterizedTypeReference<RestResponsePage<Role>> responseType = new ParameterizedTypeReference<RestResponsePage<Role>>() {
        };
        final ResponseEntity<RestResponsePage<Role>> result = restTemplate.exchange(base + "/api/v1/role", HttpMethod.GET, authHeaders, responseType);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertSame(6, result.getBody().getContent().size(), "incorrect number of elements");
        assertEquals("TESTER", result.getBody().getContent().get(0).getName(), "property value is incorrect");
    }

    @Test
    @DisplayName("Ensure role can be read by id")
    public void test_read_byId_happy(@Autowired Role role) throws NotFoundException {
        repository.save(role);
        final HttpEntity<String> authHeaders = authentication.convert("", authSuccess);
        final ResponseEntity<Role> result = restTemplate.exchange(base + "/api/v1/role/" + role.getId(), HttpMethod.GET, authHeaders, Role.class);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertEquals(role.getId(), result.getBody().getId(), "incorrect property value");
        assertEquals(role.getName(), result.getBody().getName(), "incorrect property value");
    }

    @Test
    @DisplayName("Ensure role can be updated")
    public void test_update_happy(@Autowired Role role) throws NotFoundException {
        repository.save(role);
        role.setName("SUPERTESTER");
        final HttpEntity<String> authHeaders = authentication.convert(ObjectUtil.print(role), authSuccess);
        final ResponseEntity<Role> result = restTemplate.exchange(base + "/api/v1/role/" + role.getId(), HttpMethod.PUT, authHeaders, Role.class);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertEquals(role.getId(), result.getBody().getId(), "incorrect property value");
        assertEquals("SUPERTESTER", result.getBody().getName(), "incorrect property value");
    }

    @Test
    @DisplayName("Ensure role can be deleted")
    public void test_delete_happy(@Autowired Role role) throws NotFoundException {
        repository.save(role);
        final HttpEntity<String> authHeaders = authentication.convert("", authSuccess);
        final ResponseEntity<String> result = restTemplate.exchange(base + "/api/v1/role/" + role.getId(), HttpMethod.DELETE, authHeaders, String.class);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertFalse(repository.existsById(role.getId()), "id should not exist");
    }

    @AfterEach
    public void tearDown() {
        CleanUpUtils.clean(repository);
    }

}
