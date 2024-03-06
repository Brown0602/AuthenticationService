package com.petprojects.authenticationservice.services;

import org.springframework.stereotype.Service;

@Service
public interface ValidationService {
    String validation(String user_login, String user_password, String user_email);
}
