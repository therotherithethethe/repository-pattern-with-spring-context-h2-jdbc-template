package com.arakviel.persistence.repository;

import com.arakviel.persistence.entity.GenericEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Repository<T extends GenericEntity> {

    Optional<T> findById(UUID id);
    Optional<T> findBy(String column, Object value);
    List<T> findAll();
    T save(T entity);
    int delete(UUID id);
}
