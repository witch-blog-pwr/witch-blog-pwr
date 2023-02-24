package com.witchs.blog.register;

import com.witchs.blog.email.EmailSenderService;
import com.witchs.blog.user.User;
import com.witchs.blog.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RegisterApi {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;

    @PostMapping("/auth/register")
    public ResponseEntity<?> getJwt(@RequestBody RegisterRequest registerRequest) {
        try {
            User user = new User(registerRequest.getFirstname(), registerRequest.getLastname(), registerRequest.getEmail(), passwordEncoder.encode(registerRequest.getPassword()), "USER", false);
            emailSenderService.sendVerificationEmail(registerRequest.getEmail());
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();

        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
