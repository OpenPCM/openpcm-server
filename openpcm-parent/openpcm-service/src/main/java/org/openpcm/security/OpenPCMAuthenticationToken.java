package org.openpcm.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class OpenPCMAuthenticationToken extends AbstractAuthenticationToken {

    /**
     * serial version uid
     */
    private static final long serialVersionUID = 8084617735108012767L;

    /** the token */
    private String credentials;

    private UserDetails userDetails;

    public OpenPCMAuthenticationToken(UserDetails userDetails) {
        super(userDetails.getAuthorities());
        this.userDetails = userDetails;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public UserDetails getPrincipal() {
        return userDetails;
    }

}
