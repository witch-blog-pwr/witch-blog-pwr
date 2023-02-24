package com.witchs.blog.passwordRecovery;

import com.witchs.blog.email.EmailSenderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PasswordRecoveryApi {
    private final EmailSenderService emailSenderService;
    private final PasswordRecoveryService passwordRecoveryService;
    @PostMapping("/auth/password/recovery")
    public ResponseEntity<?> passwordRecovery(@RequestParam String email) {
        emailSenderService.sendPasswordRecoveryEmail(email); //TO REFACTOR (nie wysyłać samego tokena ale z linkiem do fronetendu do strony odzyskiwania hasła i token jako parametr)
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @PostMapping("/auth/password/recovery/params")
    public ResponseEntity<?> passwordToRecovery(@RequestBody PasswordRecoveryRequest passwordRecoveryRequest) {
        passwordRecoveryService.resetPassword(passwordRecoveryRequest.getToken(), passwordRecoveryRequest.getPassword());
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}
