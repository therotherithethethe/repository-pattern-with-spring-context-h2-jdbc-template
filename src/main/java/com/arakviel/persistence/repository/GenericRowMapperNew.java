package com.arakviel.persistence.repository;

import com.arakviel.persistence.entity.GenericEntity;
import com.arakviel.persistence.repository.genericrepository.Column;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class GenericRowMapperNew<T extends GenericEntity> implements RowMapper<T> {
    private final Class<T> type;

    public GenericRowMapperNew(Class<T> type) {
        this.type = type;
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        T obj = createInstance(type);
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            String columnName = column != null ? column.name() : field.getName();
            try {
                field.setAccessible(true);
                Object value = rs.getObject(columnName);
                if (value != null) {
                    field.set(obj, value);
                }
            } catch (IllegalArgumentException | IllegalAccessException | SQLException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
        return obj;
    }


    private T createInstance(Class<T> type) {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Entity creation failed", e);
        }
    }
}
