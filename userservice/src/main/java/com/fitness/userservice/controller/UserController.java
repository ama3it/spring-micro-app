package com.fitness.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController

/*
 * @RestController: Marks the class as a REST controller. This is a shorthand
 * for @Controller + @ResponseBody, meaning every method returns JSON/XML
 * responses directly, not views.
 */

@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping("/{userid}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable String userid) {
        return ResponseEntity.ok(userService.getUserProfile(userid));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @GetMapping("/validate/{userid}")
    public ResponseEntity<Boolean> validateUser(@PathVariable String userid) {
        return ResponseEntity.ok(userService.validateUser(userid));
    }
    
}
