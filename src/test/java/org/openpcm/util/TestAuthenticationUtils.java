package org.openpcm.util;

import org.openpcm.model.AuthSuccess;
import org.openpcm.security.JWTRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public final class TestAuthenticationUtils {

    @Value("${openpcm.adminUser:admin}")
    private String adminUsername;

    @Value("${openpcm.adminPassword:openpcm}")
    private String adminPassword;

    public AuthSuccess retrieveCreds(String base) {
        final RestTemplate template = new RestTemplate();
        final AuthSuccess success = template.postForObject(base + "/authenticate/login",
                        JWTRequest.builder().username(adminUsername).password(adminPassword).build(), AuthSuccess.class);

        return success;
    }

    public HttpEntity convert(String body, AuthSuccess authSuccess) {
        final MultiValueMap headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + authSuccess.getToken().getAccess_token());
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return new HttpEntity<>(body, headers);
    }
}
