package com.arakviel.persistence.repository;

import static java.lang.StringTemplate.STR;

import com.arakviel.persistence.config.ConnectionManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public abstract class GenericRepository<T> implements Repository<T> {

    private final String tableName;
    private final JdbcTemplate jdbcTemplate;

    public GenericRepository(ConnectionManager connectionManager, String tableName) {
        this.jdbcTemplate = connectionManager.jdbcTemplate;
        this.tableName = tableName;
    }

    // Переписати на аспекти spring Context
    @Override
    public Optional<T> findById(UUID id) {
        final String sql = STR."""
            SELECT *
              FROM \{tableName}
             WHERE id = ?
        """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper(), id));
        } catch(EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<T> findAll() {
        final String sql = STR."""
            SELECT *
              FROM \{tableName}
        """;
        return jdbcTemplate.query(sql, rowMapper());
    }

    @Override
    public abstract T save(T entity);

    protected void insert(Object... values) {
        String attributesString = "id, " + String.join(", ", tableAttributes());
        String placeholders = Stream.generate(() -> "?")
            .limit(tableAttributes().size() + 1)
            .collect(Collectors.joining(", "));
        String sql = STR."""
                INSERT INTO \{tableName} (\{attributesString})
                VALUES (\{placeholders})
        """;

        jdbcTemplate.update(sql, values);
    }

    protected int update(Object... values) {
        String attributesString = tableAttributes().stream()
            .map(a -> STR."\{a} = ?")
            .collect(Collectors.joining(", "));
        String sql = STR."""
              UPDATE \{tableName}
                 SET \{attributesString}
               WHERE id = ?
        """;

        return jdbcTemplate.update(sql, values);
    }

    @Override
    public int delete(UUID id) {
        final String sql = STR."""
            DELETE FROM \{tableName}
                  WHERE id = ?
        """;
        return jdbcTemplate.update(sql, id);
    }

    protected abstract RowMapper<T> rowMapper();

    protected abstract List<String> tableAttributes();
}
