package io.bytestreme.userservice.security;

import io.bytestreme.userservice.domain.user.UsersByIdTable;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenService implements Serializable {

    @Value("${jwt.key}")
    private String SIGN_KEY;

    @Value("${jwt.claim.token-version}")
    private String TOKEN_VERSION_CLAIM;

    public String generateToken(UsersByIdTable user, String version) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(TOKEN_VERSION_CLAIM, version);

        return Jwts.builder()
                .setSubject(user.getUserId().toString())
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encode(SIGN_KEY.getBytes()))
                .compact();
    }

}