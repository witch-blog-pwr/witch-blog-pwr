package com.witchs.blog.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.witchs.blog.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TokenCreator {
    @Value("${secret.key}")
    private String KEY;
    public String createToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(KEY);
        String token = JWT.create()
                .withSubject(user.getUsername())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim("expiredTime", new Date().getTime() + (60000 * 15))
                .sign(algorithm);
        return token;
    }

    public String createToken(String username, List<SimpleGrantedAuthority> roles) {
        Algorithm algorithm = Algorithm.HMAC256(KEY);
        String token = JWT.create()
                .withSubject(username)
                .withClaim("roles", Arrays.asList(roles))
                .withClaim("expiredTime", new Date().getTime() + (60000 * 15))
                .sign(algorithm);
        return token;
    }
}
