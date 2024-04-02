package com.arakviel.persistance.repository.mapper;

import com.arakviel.persistance.entity.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
            .id(UUID.fromString(rs.getString("id")))
            .login(rs.getString("login"))
            .password(rs.getString("password"))
            .age( rs.getInt("age"))
            .build();
    }
}
