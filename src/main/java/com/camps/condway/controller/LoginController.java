package com.camps.condway.controller;

import com.camps.condway.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/login")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        if (userService.isAuthorized(email, password)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(401).body("Invalid email or passowrd");
        }
    }
}
