package org.openpcm.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openpcm.annotation.IntegrationTest;
import org.openpcm.model.AuthSuccess;
import org.openpcm.model.UserJWTTokenState;
import org.openpcm.security.JWTRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.MultiValueMap;

@Category(IntegrationTest.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class AuthenticationControllerIntTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${openpcm.adminUser:admin}")
    private String adminUsername;

    @Value("${openpcm.adminPassword:openpcm}")
    private String adminPassword;

    private String base;

    @Test
    @DisplayName("login is possible")
    public void test_login_refreshToken_worksWithGoodCredentials() {
        base = "http://localhost:" + port;
        final ResponseEntity<AuthSuccess> result = restTemplate.postForEntity(base + "/authenticate/login",
                        JWTRequest.builder().username(adminUsername).password(adminPassword).build(), AuthSuccess.class);

        assertSame(HttpStatus.OK, result.getStatusCode(), "incorrect status code");
        assertEquals(adminUsername, result.getBody().getUser().getUsername(), "property is incorrect");
        assertNotNull(result.getBody().getToken().getAccess_token(), "property is incorrect");

        final MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + result.getBody().getToken().getAccess_token());
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        final HttpEntity<String> entity = new HttpEntity<String>("", headers);

        final ResponseEntity<UserJWTTokenState> result2 = restTemplate.postForEntity(base + "/authenticate/refresh", entity, UserJWTTokenState.class);
        assertSame(HttpStatus.OK, result2.getStatusCode(), "incorrect status code");
        assertNotNull(result2.getBody().getAccess_token(), "property is incorrect");

    }

}
