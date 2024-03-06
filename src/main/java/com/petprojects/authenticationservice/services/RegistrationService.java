package com.petprojects.authenticationservice.services;

import org.springframework.stereotype.Service;

@Service
public interface RegistrationService {
    String registration(String user_login, String user_password, String email);
}
