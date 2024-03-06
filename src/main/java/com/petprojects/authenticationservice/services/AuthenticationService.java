package com.petprojects.authenticationservice.services;

import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    String authentication(String user_login, String user_password, String user_email);
}
