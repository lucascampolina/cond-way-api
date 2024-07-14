package com.camps.condway.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/connected")
@Slf4j
public class ConnectionController {

    @GetMapping
    public ResponseEntity<String> getConnected() {
        log.info("connecting");
        return ResponseEntity.ok("You're connected.");
    }
}
