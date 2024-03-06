package com.petprojects.authenticationservice.repositories;

import com.petprojects.authenticationservice.services.RegistrationService;
import com.petprojects.authenticationservice.services.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ValidationRepo implements ValidationService {

    final private RegistrationService registrationService;

    @Autowired
    public ValidationRepo(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Override
    public String validation(String user_login, String user_password, String user_email) {
        String capitals = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String chars = "'!@#$%^&*()_=+-;:,.<>?";
        for (int i = 0; i < user_password.length(); i++){
            for (int j = 0; j < chars.length(); j++){
                if (user_password.charAt(i) == chars.charAt(j)){
                    for (int l = 0; l < user_password.length(); l++){
                        for (int q = 0; q < capitals.length(); q++){
                            if (user_password.charAt(l) == capitals.charAt(q)){
                                return registrationService.registration(user_login, user_password, user_email);
                            }
                        }
                    }
                }
            }
        }
        return "Пароль не соотвествует требованиям";
    }
}

