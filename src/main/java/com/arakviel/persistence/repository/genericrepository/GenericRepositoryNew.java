package com.arakviel.persistence.repository.genericrepository;

import com.arakviel.persistence.entity.GenericEntity;
import com.arakviel.persistence.repository.GenericRowMapperNew;
import com.arakviel.persistence.repository.Repository;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

///maybe objectpool pattern

public class GenericRepositoryNew<T extends GenericEntity> implements Repository<T> {

    private final String tableName;
    private final RowMapper<T> rowMapper;
    private final JdbcTemplate jdbcTemplate;
    //private final List<Object> tableValues;
    private final List<String> tableAttributes;

    public GenericRepositoryNew(String tableName, JdbcTemplate jdbcTemplate, Class<T> clazz) {

        this.tableName = tableName;
        this.rowMapper = new GenericRowMapperNew<>(clazz);
        this.jdbcTemplate = jdbcTemplate;
        tableAttributes = tableAttributes();
    }

    @Override
    public Optional<T> findById(UUID id) {
        return findBy("id", id);
    }

    @Override
    public Optional<T> findBy(String column, Object value) {
        final String sql = STR."""
            SELECT *
              FROM \{tableName}
             WHERE \{column} = ?
        """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, value));
        } catch(EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public T insert(List<Object> values) {
        List<String> attributes = tableAttributes;
        String attributesString = "id, " + String.join(", ", attributes);
        String placeholders = Stream.generate(() -> "?")
            .limit(attributes.size() + 1)
            .collect(Collectors.joining(", "));
        String sql = STR."""
                INSERT INTO \{tableName} (\{attributesString})
                VALUES (\{placeholders})
        """;

        if (attributes.stream().anyMatch(a -> a.equals("created_at"))) {
            values.add(LocalDateTime.now()); // created_at
            values.add(LocalDateTime.now()); // updated_at
        }

        jdbcTemplate.update(sql, values.toArray());
        // TODO: write custom exception
        return findById((UUID) values.getFirst()).orElseThrow();
    }

    @Override
    public List<T> findAll() {
        final String sql = STR."""
            SELECT * FROM \{tableName}
        """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public T save(T entity) {
        List<Object> values = tableValues(entity);

        T newEntity;
        if(Objects.isNull(entity.getId())) {
            UUID newId = UUID.randomUUID();
            values.addFirst(newId);
            newEntity = insert(values);
        } else {
            values.addLast(entity.getId());

            newEntity = update(values);
        }

        return newEntity;
    }

    @Override
    public int delete(UUID id) {
        return 0;
    }

    public T update(List<Object> values) {
        List<String> attributes = tableAttributes;
        String attributesString = attributes.stream()
            .filter(a -> !a.contains("created_at"))
            .map(a -> STR."\{a} = ?")
            .collect(Collectors.joining(", "));
        String sql = STR."""
              UPDATE \{tableName}
                 SET \{attributesString}
               WHERE id = ?
        """;

        if(attributes.stream().anyMatch(a -> a.equals("updated_at"))) {
            values.add(LocalDateTime.now()); // updated_at
        }

        jdbcTemplate.update(sql, values.toArray());
        // TODO: write custom exception
        return findById((UUID) values.getLast()).orElseThrow();
    }
    private List<String> tableAttributes() {
        List<String> columns = new ArrayList<>();
        jdbcTemplate.execute((Connection connection) -> {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getColumns(null, null, tableName, null);
            while (resultSet.next()) {
                    String columnName = resultSet.getString("COLUMN_NAME");
                    if(Objects.equals(columnName, "USER_NAME")) {
                        for (int i = 0; i < 3; i++) {
                            resultSet.next();
                        }
                        continue;
                    }
                    columns.add(columnName);
            }
            return null;
        });
        return columns;
    }

    private List<Object> tableValues(T entity) {
        ArrayList<Object> values = new ArrayList<>();
        try {
            Field[] fields = entity.getClass().getDeclaredFields();

            for (int i = 1; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);

                Object value = field.get(entity);
                values.add(value);
            }
        } catch (Exception _) {
        }
        return values;
    }
}