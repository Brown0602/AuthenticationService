package com.petprojects.authenticationservice.repositories;

import com.petprojects.authenticationservice.RowMappers.UserRowMapper;
import com.petprojects.authenticationservice.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuthenticationRepo implements AuthenticationService {

    final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthenticationRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String authentication(String user_login, String user_password, String user_email){
            String sqlQuery = "SELECT * FROM restfulapiusers WHERE user_login = ? AND user_password = ? AND user_email = ?";
            jdbcTemplate.queryForObject(sqlQuery, new UserRowMapper(), String.valueOf(user_login.hashCode()), String.valueOf(user_password.hashCode()), String.valueOf(user_email.hashCode()));
            return sqlQuery;
    }
}
