package com.petprojects.authenticationservice.repositories;

import com.petprojects.authenticationservice.dto.UserDTO;
import com.petprojects.authenticationservice.services.DeleteCodeConfirmationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

@Repository
public class DeleteCodeConfirmationRepo implements DeleteCodeConfirmationService {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public DeleteCodeConfirmationRepo(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void deleteCodeConfirmation(String code_confirmation) {
        String sqlQuery = "UPDATE restfulapiusers SET code_confirmation = ? WHERE code_confirmation = ?";
        jdbcTemplate.update(sqlQuery, null, code_confirmation);
    }
}
