package com.petprojects.authenticationservice.services;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface MailConfirmationService {
    String mailConfirmation(String email) throws IOException, MessagingException;

}
