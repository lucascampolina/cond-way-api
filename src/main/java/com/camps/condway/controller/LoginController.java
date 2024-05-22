package com.camps.condway.controller;

import com.camps.condway.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/login")
@RequiredArgsConstructor
public class LoginController {

    private final UserServiceImpl userServiceImpl;

    @GetMapping
    public ResponseEntity<?> login(@RequestParam String email) {
        UserDetails userDetails = userServiceImpl.loadUserByUsername(email);
        if (userDetails.isEnabled()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(401).body("Invalid email or passowrd");
        }
    }
}
