package com.springboot.api.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Utils
 */
public class Utils {

    public static Authentication provideAuthentication() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ADMIN"));
        Authentication authentication= new UsernamePasswordAuthenticationToken("root", "toor", authorities);
        return authentication;
    }
}