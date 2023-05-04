package com.example.demo.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.UUID;




@RestController
public class UserRegistrationController {

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> registerUser(@RequestBody UserRegistrationRequest request) {
        // Validate request parameters
        if (StringUtils.isBlank(request.getUsername()) || StringUtils.isBlank(request.getPassword()) || StringUtils.isBlank(request.getIpAddress())) {
            return ResponseEntity.badRequest().body(new UserRegistrationResponse("All fields are required"));
        }

        if (request.getPassword().length() < 8 ||
                !request.getPassword().matches(".*[0-9].*") ||
                !request.getPassword().matches(".*[A-Z].*") ||
                !request.getPassword().matches(".*[_#$%\\.].*")) {
            return ResponseEntity.badRequest().body(new UserRegistrationResponse("Password must be at least 8 characters long and contain at least 1 number, 1 capitalized letter, and 1 special character in this set '_#$%."));
        }

        // Call IP-API.com to get geolocation for provided IP
        String url = "http://ip-api.com/json/" + request.getIpAddress();
        ResponseEntity<IpApiResponse> responseEntity = restTemplate.getForEntity(url, IpApiResponse.class);

        if (!responseEntity.getBody().getCountryCode().equalsIgnoreCase("CA")) {
            return ResponseEntity.badRequest().body(new UserRegistrationResponse("User is not eligible to register"));
        }

        // Generate UUID and welcome message with city name
        String uuid = UUID.randomUUID().toString();
        String cityName = responseEntity.getBody().getCity();
        String message = "Welcome " + request.getUsername() + "! You have been registered successfully from " + cityName;

        UserRegistrationResponse response = new UserRegistrationResponse(uuid, message);
        return ResponseEntity.ok(response);
    }
