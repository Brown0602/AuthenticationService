package com.petprojects.authenticationservice.repositories;

import com.petprojects.authenticationservice.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RegistrationRepo implements RegistrationService {

    final private JdbcTemplate jdbcTemplate;

    @Autowired
    public RegistrationRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String registration(String user_login, String user_password, String user_email) {
        String sqlQuery = "INSERT INTO restfulapiusers(user_login, user_password, user_email) VALUES(?, ?, ?)";
        jdbcTemplate.update(sqlQuery, user_login.hashCode(), user_password.hashCode(), user_email.hashCode());
        return sqlQuery;
    }
}
