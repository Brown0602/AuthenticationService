package com.petprojects.authenticationservice.services;

import org.springframework.stereotype.Service;

@Service
public interface SaveCodeConfirmationService {
    void saveCodeConfirmation(String codeConfirmation, String email);
}
