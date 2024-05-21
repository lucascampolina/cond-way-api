package com.camps.condway.controller;

import com.camps.condway.entity.User;
import com.camps.condway.service.EmailService;
import com.camps.condway.service.TemplateService;
import com.camps.condway.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;

    private final EmailService emailService;

    private final TemplateService templateService;

    public UserController(UserService userService, EmailService emailService, TemplateService templateService) {
        this.userService = userService;
        this.emailService = emailService;
        this.templateService = templateService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        try {
            userService.register(user);

            Context context = new Context();
            context.setVariable("firstName", user.getFirstName());
            String emailContent = templateService.generateEmailContent("welcome-template", context);

            CompletableFuture<Void> emailFuture = emailService.sendEmail(user.getEmail(), "Seu acesso ao CondWay!", emailContent);
            isEmailFailed(emailFuture);

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(user);
        }
    }

    private static CompletableFuture<Boolean> isEmailFailed(CompletableFuture<Void> emailFuture) {
        return emailFuture.handle((result, ex) -> {
           if (ex != null) {
               log.error("Erro ao enviar email {}", ex.getMessage());
               return false;
           }
            return true;
        });
    }

    @GetMapping()
    public ResponseEntity<List<User>> findAll() {
       List<User> users = userService.findAll();
       return ResponseEntity.ok(users);

    }
}
