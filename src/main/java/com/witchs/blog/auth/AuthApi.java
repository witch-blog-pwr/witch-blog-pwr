package com.witchs.blog.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.witchs.blog.security.TokenCreator;
import com.witchs.blog.user.User;
import com.witchs.blog.user.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.stream.Collectors;

@RestController
public class AuthApi {
    @Value("${secret.key}")
    private String KEY;
    private final TokenCreator tokenCreator;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthApi(AuthenticationManager authenticationManager, UserRepository userRepository, TokenCreator tokenCreator) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenCreator = tokenCreator;
    }
    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());
            Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            User user = (User) authenticate.getPrincipal();
            String token = tokenCreator.createToken(user);
            AuthResponse authResponse = new AuthResponse(user.getEmail(), token);
            return ResponseEntity.ok(authResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(e);
        }
    }
}
