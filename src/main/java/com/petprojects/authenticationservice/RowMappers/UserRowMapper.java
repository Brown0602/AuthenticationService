package com.petprojects.authenticationservice.RowMappers;

import com.petprojects.authenticationservice.models.User;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setUser_id(rs.getLong("user_id"));
        user.setUser_login(rs.getString("user_login"));
        user.setUser_password(rs.getString("user_password"));
        user.setUser_email(rs.getString("user_email"));
        user.setCode_confirmation(rs.getString("code_confirmation"));
        return user;
    }
}
