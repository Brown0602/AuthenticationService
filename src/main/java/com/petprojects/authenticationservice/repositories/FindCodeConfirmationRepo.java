package com.petprojects.authenticationservice.repositories;

import com.petprojects.authenticationservice.RowMappers.UserRowMapper;
import com.petprojects.authenticationservice.services.FindCodeConfirmationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FindCodeConfirmationRepo implements FindCodeConfirmationService {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public FindCodeConfirmationRepo(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String findCodeConfirmation(String codeConfirmation) {
        String sqlQuery = "SELECT * FROM restfulapiusers WHERE code_confirmation = ?";
        jdbcTemplate.queryForObject(sqlQuery, new UserRowMapper(), codeConfirmation);
        return sqlQuery;
    }
}
