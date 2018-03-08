package org.openpcm.security;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultJwtBuilder;

@Component
public class TokenHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenHelper.class);

    @Value("${spring.application.name}")
    private String APP_NAME;

    @Value("${jwt.secret}")
    public String SECRET;

    @Value("${jwt.expires_in}")
    private int EXPIRES_IN;

    @Value("${jwt.header}")
    private String AUTH_HEADER;

    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    public String getUsernameFromToken(String token) {
        String username;
        final Claims claims = this.getAllClaimsFromToken(token);
        username = claims.getSubject();

        return username;
    }

    public Date getIssuedAtDateFromToken(String token) {
        Date issueAt;

        final Claims claims = this.getAllClaimsFromToken(token);
        issueAt = claims.getIssuedAt();
        return issueAt;
    }

    public String getAudienceFromToken(String token) {
        String audience;
        final Claims claims = this.getAllClaimsFromToken(token);
        audience = claims.getAudience();
        return audience;
    }

    public Date getExpirationFromToken(String token) {
        Date expiration;

        final Claims claims = this.getAllClaimsFromToken(token);
        expiration = claims.getExpiration();

        return expiration;
    }

    public String refreshToken(String token) {
        String refreshedToken;
        Date now = new Date();
        final Claims claims = this.getAllClaimsFromToken(token);
        Date expiration = new Date((Integer) claims.get(Claims.EXPIRATION));

        if (now.after(expiration)) {
            LOGGER.trace("Token not expired, returning same token.");
            return token;
        } else {
            LOGGER.trace("Token expired, generating new token.");
            claims.setIssuedAt(now);
            refreshedToken = Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate()).signWith(SIGNATURE_ALGORITHM, SECRET).compact();
            return refreshedToken;

        }

    }

    public String generateToken(String username) {

        JwtBuilder builder = Jwts.builder().setIssuer(APP_NAME).setSubject(username).setAudience("web").setIssuedAt(new Date())
                        .setExpiration(generateExpirationDate()).signWith(SIGNATURE_ALGORITHM, SECRET);

        if (LOGGER.isDebugEnabled()) {

            Field header = ReflectionUtils.findField(DefaultJwtBuilder.class, "header");
            header.setAccessible(true);
            Field claims = ReflectionUtils.findField(DefaultJwtBuilder.class, "claims");
            claims.setAccessible(true);

            try {
                LOGGER.debug("generated jwt: \r\n{}\r\n{}", header != null ? header.get(builder) : null, claims != null ? claims.get(builder) : null);
            } catch (IllegalArgumentException e) {
                LOGGER.warn(e.getMessage(), e);
            } catch (IllegalAccessException e) {
                LOGGER.warn(e.getMessage(), e);
            } finally {
                header.setAccessible(false);
                claims.setAccessible(false);
            }
        }

        return builder.compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();

        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(Instant.now().toEpochMilli() + EXPIRES_IN * 1000);
    }

    public int getExpiredIn() {
        return EXPIRES_IN;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        final Date created = getIssuedAtDateFromToken(token);
        final Date expiration = getExpirationFromToken(token);

        LOGGER.trace("exiration: {}. Right now: {}", expiration.toString(), new Date().toString());

        return (username != null && username.equals(userDetails.getUsername()) && created != null && expiration != null);
    }

    public String getToken(HttpServletRequest request) {
        /**
         * Getting the token from Authentication header e.g Bearer your_token
         */
        String authHeader = getAuthHeaderFromHeader(request);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    public String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER);
    }
}
