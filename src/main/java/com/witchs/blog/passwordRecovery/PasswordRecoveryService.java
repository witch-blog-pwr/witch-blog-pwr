package com.witchs.blog.passwordRecovery;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.witchs.blog.user.User;
import com.witchs.blog.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordRecoveryService {
    @Value("${password.recovery.key}")
    private String PASSWORDRECOVERYKEY;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordRecoveryService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void resetPassword(String token, String password) {
        Algorithm algorithm = Algorithm.HMAC256(PASSWORDRECOVERYKEY);
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        User user = userRepository.findByEmail(jwt.getSubject()).get();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
}
