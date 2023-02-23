package com.witchs.blog.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApi {

    @Value("${secret.key}")
    private String KEY;

    @GetMapping("/key")
    public String getSecretKey() {
        return KEY;
    }
}
