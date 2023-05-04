package com.example.demo.entity; 

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate; 



static class UserRegistrationResponse {
        private String uuid;
        private String message;

        public UserRegistrationResponse(String errorMessage) {
            this.message = errorMessage;
        }

        public UserRegistrationResponse(String uuid, String message) {
            this.uuid = uuid;
            this.message = message;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }