package com.petprojects.authenticationservice.repositories;

import com.petprojects.authenticationservice.RowMappers.UserRowMapper;
import com.petprojects.authenticationservice.services.FindEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FindEmailRepo implements FindEmailService {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public FindEmailRepo(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void findEmail(String email) {
        String sqlQuery = "SELECT * FROM restfulapiusers WHERE user_email = ?";
        jdbcTemplate.queryForObject(sqlQuery, new UserRowMapper(), String.valueOf(email.hashCode()));
    }
}
