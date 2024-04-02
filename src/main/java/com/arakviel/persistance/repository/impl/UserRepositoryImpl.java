package com.arakviel.persistance.repository.impl;

import com.arakviel.persistance.config.ConnectionManager;
import com.arakviel.persistance.entity.User;
import com.arakviel.persistance.repository.GenericRepository;
import com.arakviel.persistance.repository.contracts.UserRepository;
import com.arakviel.persistance.repository.mapper.UserMapper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoryImpl
    extends GenericRepository<User>
    implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(ConnectionManager connectionManager) {
        super(connectionManager, "users");
        this.jdbcTemplate = connectionManager.jdbcTemplate;
    }

    @Override
    public User save(User user) {
        if(Objects.isNull(user.getId())) {
            UUID newId = UUID.randomUUID();
            user.setId(newId);
            insert(newId, user.getLogin(), user.getPassword(), user.getAge());
        } else {
            update(user.getLogin(), user.getPassword(), user.getAge(), user.getId());
        }

        return user;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        String sql = """
            SELECT * 
              FROM users 
             WHERE login = ?
            """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper(), login));
        } catch(EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    protected RowMapper<User> rowMapper() {
        return new UserMapper();
    }

    @Override
    protected List<String> tableAttributes() {
        return List.of("login", "password", "age");
    }
}
