package com.arakviel.persistence.repository.contracts;

import com.arakviel.persistence.entity.User;
import com.arakviel.persistence.repository.Repository;
import java.util.Optional;

public interface UserRepository extends Repository<User> {

    Optional<User> findByLogin(String login);
}
