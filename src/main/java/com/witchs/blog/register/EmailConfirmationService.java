package com.witchs.blog.register;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.witchs.blog.user.User;
import com.witchs.blog.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EmailConfirmationService {
    @Value("${email.secret.key}")
    private String EMAILKEY;

    private final UserRepository userRepository;

    public EmailConfirmationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void confirmEmail(String token) {
        Algorithm algorithm = Algorithm.HMAC256(EMAILKEY);
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        if(jwt.getClaim("expiredTime").asLong() > new Date().getTime()) {
            User user = userRepository.findByEmail(jwt.getSubject()).get();
            user.setEmailVerified(true);
            userRepository.save(user);
        }
    }
}
