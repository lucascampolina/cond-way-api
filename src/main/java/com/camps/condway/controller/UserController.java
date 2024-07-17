package com.camps.condway.controller;

import com.camps.condway.entity.User;
import com.camps.condway.service.EmailService;
import com.camps.condway.service.TemplateService;
import com.camps.condway.service.UserServiceImpl;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserServiceImpl userServiceImpl;
    private final EmailService emailService;
    private final TemplateService templateService;

    public UserController(UserServiceImpl userServiceImpl, EmailService emailService, TemplateService templateService) {
        this.userServiceImpl = userServiceImpl;
        this.emailService = emailService;
        this.templateService = templateService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        try {
            Optional<User> userOpt = userServiceImpl.register(user);
            if (userOpt.isPresent()) {
                User registeredUser = userOpt.get();
//                CompletableFuture<Void> emailFuture = sendAsyncEmail(registeredUser);
//                emailFuture.handle((result, ex) -> {
//                    if (ex != null) {
//                        log.error("Erro ao enviar email {}", ex.getMessage());
//                    }
//                    return null;
//                });
                return ResponseEntity.ok(registeredUser);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(user);
            }
        } catch (Exception e) {
            log.error("Erro ao registrar usuario com o email: {}", user.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(user);
        }
    }

    private CompletableFuture<Void> sendAsyncEmail(User user) throws MessagingException {
        Context context = new Context();
        context.setVariable("firstName", user.getFirstName());
        String emailContent = templateService.generateEmailContent("welcome-template", context);
        return emailService.sendEmail(user.getEmail(), "Seu acesso ao CondWay!", emailContent);
    }


    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> users = userServiceImpl.findAll();
        return ResponseEntity.ok(users);
    }
}
