package com.petprojects.authenticationservice.controlles;

import com.petprojects.authenticationservice.dto.UserDTO;
import com.petprojects.authenticationservice.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ControllerAuthentication {

    final AuthenticationService authenticationService;

    @Autowired
    public ControllerAuthentication(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @RequestMapping(value = "/authentication", method = RequestMethod.POST)
    public String authentication(@RequestBody UserDTO userDTO){
        try {
            return authenticationService.authentication(userDTO.getUser_login(), userDTO.getUser_password(), userDTO.getUser_email());
        }catch (Exception exception){
            return "Пользователь с такими данными не найден";
        }
    }

}
