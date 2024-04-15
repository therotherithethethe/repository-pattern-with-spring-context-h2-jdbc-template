package com.arakviel.persistence.repository.impl;

import com.arakviel.persistence.config.ConnectionManager;
import com.arakviel.persistence.entity.User;
import com.arakviel.persistence.repository.genericrepository.GenericRepository;
import com.arakviel.persistence.repository.contracts.UserRepository;
import com.arakviel.persistence.repository.mapper.UserMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl
    extends GenericRepository<User>
    implements UserRepository {

    public UserRepositoryImpl(ConnectionManager connectionManager, UserMapper userMapper) {
        super(connectionManager, "users", userMapper);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return findBy("login", login);
    }

    @Override
    protected List<String> tableAttributes() {
        return List.of("login", "password", "age");
    }

    @Override
    protected List<Object> tableValues(User user) {
        ArrayList<Object> values = new ArrayList<>();
        values.add(user.getLogin());
        values.add(user.getPassword());
        values.add(user.getAge());
        return values;
    }
}
