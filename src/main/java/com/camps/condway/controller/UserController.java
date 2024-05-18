package com.camps.condway.controller;

import com.camps.condway.entity.User;
import com.camps.condway.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    @GetMapping()
    public ResponseEntity<List<User>> findAll() {
       List<User> users = userService.findAll();
       return ResponseEntity.ok(users);

    }
}
