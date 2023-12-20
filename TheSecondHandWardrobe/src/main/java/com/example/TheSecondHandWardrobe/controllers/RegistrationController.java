package com.example.TheSecondHandWardrobe.controllers;

import com.example.TheSecondHandWardrobe.data.RegistrationRequest;
import com.example.TheSecondHandWardrobe.services.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

//    @PostMapping
//    public ResponseEntity<Object> register(@ModelAttribute RegistrationRequest request) {
//        System.out.println("register controller");
//        return registrationService.register(request);
//    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}