package com.witchs.blog.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.witchs.blog.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenCreator {
    @Value("${secret.key}")
    private String KEY;
    @Value("${email.secret.key}")
    private String EMAILKEY;

    @Value("${password.recovery.key}")
    private String PASSWORDRECOVERYKEY;

    public String createToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(KEY);
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim("expiredTime", new Date().getTime() + (60000 * 60 * 24))
                .sign(algorithm);
    }

    public String createEmailConfirmationToken(String email) {
        Algorithm algorithm = Algorithm.HMAC256(EMAILKEY);
        return JWT.create()
                .withSubject(email)
                .withClaim("expiredTime", new Date().getTime() + (60000 * 10))
                .sign(algorithm);
    }

    public String createPasswordRecoveryToken(String email) {
        Algorithm algorithm = Algorithm.HMAC256(PASSWORDRECOVERYKEY);
        return JWT.create()
                .withSubject(email)
                .withClaim("expiredTime", new Date().getTime() + (60000 * 10))
                .sign(algorithm);
    }
}
