package com.arakviel.persistance.config;

import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

@Component
public class ConnectionManager {

    public final JdbcTemplate jdbcTemplate;

    public ConnectionManager() {
        var dataSource = new DriverManagerDataSource("jdbc:h2:file: ./db/blog", "", "");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        seed();
    }

    private void seed() {
        String createSql = """
            DROP TABLE IF EXISTS users;
            CREATE TABLE IF NOT EXISTS users(
                id          UUID PRIMARY KEY,
                login       VARCHAR(64) NOT NULL,
                password    VARCHAR(128) NOT NULL,
                age         INT NOT NULL
            )
            """;
        jdbcTemplate.execute(createSql);

        String insertSql = "INSERT INTO users(id, login, password, age) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(insertSql, UUID.randomUUID(),"boklah", "tro-gernamy-228", 17);
        jdbcTemplate.update(insertSql, UUID.randomUUID(),"geletiy", "password", 17);
        jdbcTemplate.update(insertSql, UUID.randomUUID(),"romanko", "qwerty", 17);
        jdbcTemplate.update(insertSql, UUID.randomUUID(),"andrusyk", "aifan777", 17);
        jdbcTemplate.update(insertSql, UUID.randomUUID(),"lyamceva", "scoro_v_obshagu", 17);
    }
}
