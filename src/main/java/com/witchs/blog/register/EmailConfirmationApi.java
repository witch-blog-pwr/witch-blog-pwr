package com.witchs.blog.register;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class EmailConfirmationApi {
    private final EmailConfirmationService emailConfirmationService;

    @GetMapping("/auth/email/confirmation")
    public ResponseEntity<?> emailConfirmation(@RequestParam String token) {
        emailConfirmationService.confirmEmail(token);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}
