package com.petprojects.authenticationservice.controlles;

import com.petprojects.authenticationservice.dto.UserDTO;
import com.petprojects.authenticationservice.services.FindEmailService;
import com.petprojects.authenticationservice.services.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ControllerRegistration {

    final FindEmailService findEmailService;
    final ValidationService validationService;

    @Autowired
    public ControllerRegistration(FindEmailService findEmailService, ValidationService validationService) {
        this.findEmailService = findEmailService;
        this.validationService = validationService;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@RequestBody UserDTO userDTO){
        try {
            findEmailService.findEmail(userDTO.getUser_email());
            return "Пользователь с таким email уже существует";
        }catch (Exception exception) {
            return validationService.validation(userDTO.getUser_login(), userDTO.getUser_password(), userDTO.getUser_email());
        }
    }
}
