package com.petprojects.authenticationservice.controlles;

import com.petprojects.authenticationservice.dto.UserDTO;
import com.petprojects.authenticationservice.services.AuthenticationService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
public class ControllerAuthentication {

    final private AuthenticationService authenticationService;

    @Autowired
    public ControllerAuthentication(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @RequestMapping(value = "/authentication", method = RequestMethod.POST)
    public String authentication(@RequestBody UserDTO userDTO) throws MessagingException, IOException {
        return authenticationService.authentication(userDTO.getUser_login(), userDTO.getUser_password(), userDTO.getUser_email());

    }

}
