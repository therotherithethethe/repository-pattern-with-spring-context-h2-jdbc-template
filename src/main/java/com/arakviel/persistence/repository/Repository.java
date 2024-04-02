package com.arakviel.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Repository<T> {

    Optional<T> findById(UUID id);
    List<T> findAll();
    T save(T entity);
    int delete(UUID id);
}
