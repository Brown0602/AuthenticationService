package com.petprojects.authenticationservice.repositories;

import com.petprojects.authenticationservice.services.SaveCodeConfirmationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SaveCodeConfirmationRepo implements SaveCodeConfirmationService {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public SaveCodeConfirmationRepo(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveCodeConfirmation(String codeConfirmation, String email) {
        String sqlQuery = "UPDATE restfulapiusers SET code_confirmation = ? WHERE user_email = ?";
        jdbcTemplate.update(sqlQuery, codeConfirmation, email);
    }
}
