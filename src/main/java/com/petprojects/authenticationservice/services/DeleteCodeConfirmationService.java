package com.petprojects.authenticationservice.services;

import com.petprojects.authenticationservice.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public interface DeleteCodeConfirmationService {
    void deleteCodeConfirmation(String code_confirmation);
}
