package com.witchs.blog.register;

import com.witchs.blog.email.EmailSenderService;
import com.witchs.blog.user.User;
import com.witchs.blog.user.UserRepository;

import java.lang.IllegalArgumentException;
import java.util.regex.*;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
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
    private static final String validationRegex =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])" +
        "(?=.*[@#$%^&+=])(?=\\S+$).{8,64}$";
    private static final Pattern validationPattern =
        Pattern.compile(validationRegex);

    @PostMapping("/auth/register")
    public ResponseEntity < ? > getJwt(@RequestBody RegisterRequest registerRequest) {
        try {
            String password = registerRequest.getPassword();
            validatePassword(password);
            String email = registerRequest.getEmail();
            validateEmail(email);
            User user = new User(registerRequest.getFirstname(), registerRequest.getLastname(),
                registerRequest.getEmail(), passwordEncoder.encode(password),
                "USER", false);
            emailSenderService.sendVerificationEmail(registerRequest.getEmail());
            userRepository.save(user);
            return ResponseEntity.ok().build();

        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception);
        }
    }

    private void validatePassword(String password) throws IllegalArgumentException {
        if (password == null) {
            throw new IllegalArgumentException("Null as password");
        }

        Matcher matcher = validationPattern.matcher(password);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Password policy incompatible");
        }
    }

    private void validateEmail(String email) throws IllegalArgumentException {
        if(userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
    }
}
