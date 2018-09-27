package org.openpcm.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openpcm.annotation.IntegrationTest;
import org.openpcm.dao.UserRepository;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.AuthSuccess;
import org.openpcm.model.User;
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
public class UserControllerIntTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserControllerIntTest.class);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestAuthenticationUtils authentication;

    @Autowired
    private UserRepository repository;

    private AuthSuccess authSuccess;

    private String base;

    @BeforeEach
    public void beforeEach() {
        base = "http://localhost:" + port;
        authSuccess = authentication.retrieveCreds(base);
    }

    @Test
    @DisplayName("Ensure user can be created")
    public void test_createSucceeds(@Autowired User user) throws URISyntaxException {
        final HttpEntity<String> authHeaders = authentication.convert(ObjectUtil.print(user), authSuccess);
        final ResponseEntity<User> result = restTemplate.exchange(base + "/api/v1/user", HttpMethod.POST, authHeaders, User.class);

        assertSame(HttpStatus.CREATED, result.getStatusCode(), "incorrect status code");
        assertNotNull(result.getBody().getId(), "instance should not be null");
        assertEquals("demo", result.getBody().getUsername(), "property value is incorrect");
    }

    @Test
    @DisplayName("Ensure user can be read")
    public void test_read_pagination_happy(@Autowired User user) throws JsonParseException, JsonMappingException, IOException {
        repository.save(user);
        final HttpEntity<String> authHeaders = authentication.convert("", authSuccess);
        final ParameterizedTypeReference<RestResponsePage<User>> responseType = new ParameterizedTypeReference<RestResponsePage<User>>() {
        };
        final ResponseEntity<RestResponsePage<User>> result = restTemplate.exchange(base + "/api/v1/user", HttpMethod.GET, authHeaders, responseType);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertSame(2, result.getBody().getContent().size(), "incorrect number of elements");
        assertEquals("demo", result.getBody().getContent().get(0).getUsername(), "property value is incorrect");
    }

    @Test
    @DisplayName("Ensure user can be read by id")
    public void test_read_byId_happy(@Autowired User user) throws NotFoundException {
        repository.save(user);
        final HttpEntity<String> authHeaders = authentication.convert("", authSuccess);
        final ResponseEntity<User> result = restTemplate.exchange(base + "/api/v1/user/" + user.getId(), HttpMethod.GET, authHeaders, User.class);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertEquals(user.getId(), result.getBody().getId(), "incorrect property value");
        assertEquals(user.getUsername(), result.getBody().getUsername(), "incorrect property value");
    }

    @Test
    @DisplayName("Ensure user can be updated")
    public void test_update_happy(@Autowired User user) throws NotFoundException {
        repository.save(user);
        user.setUsername("DEMOUSER");
        final HttpEntity<String> authHeaders = authentication.convert(ObjectUtil.print(user), authSuccess);
        final ResponseEntity<User> result = restTemplate.exchange(base + "/api/v1/user/" + user.getId(), HttpMethod.PUT, authHeaders, User.class);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertEquals(user.getId(), result.getBody().getId(), "incorrect property value");
        assertEquals("DEMOUSER", result.getBody().getUsername(), "incorrect property value");
    }

    @Test
    @DisplayName("Ensure user can be deleted")
    public void test_delete_happy(@Autowired User user) throws NotFoundException {
        repository.save(user);
        final HttpEntity<String> authHeaders = authentication.convert("", authSuccess);
        final ResponseEntity<String> result = restTemplate.exchange(base + "/api/v1/user/" + user.getId(), HttpMethod.DELETE, authHeaders, String.class);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertFalse(repository.existsById(user.getId()), "id should not exist");
    }

    @AfterEach
    public void tearDown() {
        final List<String> defaultUsers = Arrays.asList("admin");
        repository.findAll().forEach(user -> {
            if (!defaultUsers.contains(user.getUsername())) {
                LOGGER.warn("Deleting user: {}", user.getUsername());
                repository.deleteById(user.getId());
            }
        });
    }
}
