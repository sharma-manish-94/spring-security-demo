package com.spring.securitydemo.controller;

import com.spring.securitydemo.model.Customer;
import com.spring.securitydemo.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        if (Objects.isNull(customer)) {
            return ResponseEntity.badRequest().body("Customer cannot be null");
        }
        if (Objects.isNull(customer.getEmail()) || Objects.isNull(customer.getPwd())) {
            return ResponseEntity.badRequest().body("Email and Password cannot be null");
        }
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        try {
            customer.setPwd(passwordEncoder.encode(customer.getPwd()));
            final Customer savedCustomer = customerRepository.save(customer);
            if (savedCustomer.getId() > 0) {
                return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
            } else {
                return ResponseEntity.badRequest().body("User registration failed");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error: " + e.getMessage());
        }
    }
}
