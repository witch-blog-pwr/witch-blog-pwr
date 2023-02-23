package com.witchs.blog.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Credentials {
    private Long expiredTime;
    private String username;
    private List<SimpleGrantedAuthority> collect;
}
