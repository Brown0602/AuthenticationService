package com.petprojects.authenticationservice.services;

import com.petprojects.authenticationservice.dto.UserDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public interface ForgotDataService {
    String forgotData(UserDTO userDTO, HttpSession httpSession);
}
