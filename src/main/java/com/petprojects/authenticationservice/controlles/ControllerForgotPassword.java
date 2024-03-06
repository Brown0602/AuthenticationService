package com.petprojects.authenticationservice.controlles;

import com.petprojects.authenticationservice.RowMappers.UserRowMapper;
import com.petprojects.authenticationservice.dto.UserDTO;
import com.petprojects.authenticationservice.models.User;
import com.petprojects.authenticationservice.services.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class ControllerForgotPassword {

    JdbcTemplate jdbcTemplate;

    final FindEmailService findEmailService;
    final ForgotDataService forgotDataService;
    final MailConfirmationService mailConfirmationService;
    final FindCodeConfirmationService findCodeConfirmationService;
    final DeleteCodeConfirmationService deleteCodeConfirmationService;

    @Autowired
    public ControllerForgotPassword(JdbcTemplate jdbcTemplate, FindEmailService findEmailService, ForgotDataService forgotDataService, MailConfirmationService mailConfirmationService, FindCodeConfirmationService findCodeConfirmationService, DeleteCodeConfirmationService deleteCodeConfirmationService){
        this.jdbcTemplate = jdbcTemplate;
        this.findEmailService = findEmailService;
        this.forgotDataService = forgotDataService;
        this.mailConfirmationService = mailConfirmationService;
        this.findCodeConfirmationService = findCodeConfirmationService;
        this.deleteCodeConfirmationService = deleteCodeConfirmationService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<User>> forgotPassword(){
        return ResponseEntity.ok(jdbcTemplate.query("SELECT * FROM restfulapiusers", new UserRowMapper()));
    }

    @RequestMapping(value = "/forgot", method = RequestMethod.POST)
    public String forgotPassword(@RequestBody UserDTO userDTO, HttpSession httpSession) throws MessagingException, IOException {
        if (userDTO.getUser_login() == null && userDTO.getUser_password() == null && userDTO.getUser_email() != null && userDTO.getCode_confirmation() == null) {
            try{
                findEmailService.findEmail(userDTO.getUser_email());
                mailConfirmationService.mailConfirmation(userDTO.getUser_email());
            }catch (Exception exception){
                return "Пользователь с таким email не существует";
            }
        } else if (userDTO.getUser_login() == null && userDTO.getUser_password() == null && userDTO.getUser_email() == null && userDTO.getCode_confirmation() != null) {
            try {
                findCodeConfirmationService.findCodeConfirmation(userDTO.getCode_confirmation());
                httpSession.setAttribute("code_confirmation", userDTO.getCode_confirmation());
            }catch (Exception exception){
                return "Неверный код водтверждения";
            }
        }else {
            forgotDataService.forgotData(userDTO, httpSession);
            deleteCodeConfirmationService.deleteCodeConfirmation(String.valueOf(httpSession.getAttribute("code_confirmation")));
        }
        return "";
    }
}
