package com.eyenet.config;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

@Configuration
public class JwtConfig {
    
    @Value("${eyenet.security.jwt.secret}")
    private String jwtSecret;
    
    @Value("${eyenet.security.jwt.expiration}")
    private long jwtExpiration;
    
    private Key key;
    
    public Key getKey() {
        if (key == null) {
            byte[] decodedKey = Base64.getDecoder().decode(jwtSecret);
            key = new SecretKeySpec(decodedKey, SignatureAlgorithm.HS512.getJcaName());
        }
        return key;
    }
    
    public long getExpiration() {
        return jwtExpiration;
    }
}
