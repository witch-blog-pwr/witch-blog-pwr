package com.witchs.blog.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.witchs.blog.user.User;
import com.witchs.blog.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Value("${secret.key}")
    private String KEY;
    private final TokenCreator tokenCreator;

    private final UserRepository userRepository;

    public JwtTokenFilter(TokenCreator tokenCreator, UserRepository userRepository) {
        this.tokenCreator = tokenCreator;
        this.userRepository = userRepository;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = getUsernamePasswordAuthenticationToken(authorization);
        Long expiredTime = ((Credentials) usernamePasswordAuthenticationToken.getCredentials()).getExpiredTime();
        String username = ((Credentials) usernamePasswordAuthenticationToken.getCredentials()).getUsername();
        User user = userRepository.findByEmail(username).get();
        if(expiredTime < new Date().getTime()) {
            filterChain.doFilter(request, response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        String token = tokenCreator.createToken(user);
        response.addHeader("Token", token);
        filterChain.doFilter(request, response);
    }

    public UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(KEY);
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        DecodedJWT jwt = verifier.verify(token.substring(7));
        String[] roles = jwt.getClaim("roles").asArray(String.class);
        Long expiredTime = jwt.getClaim("expiredTime").asLong();
        List<SimpleGrantedAuthority> collect = Stream.of(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        Credentials credentials = new Credentials(expiredTime, jwt.getSubject(), collect);

        return new UsernamePasswordAuthenticationToken(jwt.getSubject(), credentials, collect);
    }
}
