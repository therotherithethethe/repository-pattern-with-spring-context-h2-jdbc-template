package com.arakviel.persistance.repository.contracts;

import com.arakviel.persistance.entity.User;
import com.arakviel.persistance.repository.Repository;
import java.util.Optional;

public interface UserRepository extends Repository<User> {

    Optional<User> findByLogin(String login);
}
