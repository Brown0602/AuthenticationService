package com.petprojects.authenticationservice.services;

import org.springframework.stereotype.Service;

@Service
public interface FindEmailService {
    void findEmail(String email);
}
