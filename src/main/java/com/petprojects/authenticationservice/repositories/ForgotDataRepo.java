package com.petprojects.authenticationservice.repositories;

import com.petprojects.authenticationservice.dto.UserDTO;
import com.petprojects.authenticationservice.services.ForgotDataService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ForgotDataRepo implements ForgotDataService {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public ForgotDataRepo(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String forgotData(UserDTO userDTO, HttpSession httpSession) {
        String sqlQuery = "UPDATE restfulapiusers SET user_login = ?, user_password = ? WHERE code_confirmation = ?";
        jdbcTemplate.update(sqlQuery, String.valueOf(userDTO.getUser_login().hashCode()), String.valueOf(userDTO.getUser_password().hashCode()), httpSession.getAttribute("code_confirmation"));
        return sqlQuery;
    }
}
