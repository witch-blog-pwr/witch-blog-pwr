package com.witchs.blog.passwordRecovery;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PasswordRecoveryRequest {
    private String token;
    private String password;
}
