package com.arakviel.persistence.config;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

@Component
public class ConnectionManager {

    public final JdbcTemplate jdbcTemplate;
    private final UUID author1 = UUID.randomUUID();
    private final UUID author2 = UUID.randomUUID();

    public ConnectionManager() {
        var dataSource = new DriverManagerDataSource("jdbc:h2:file: ./db/blog", "", "");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        seed();
    }

    private void seed() {
        createUserTable();
        createPostTable();

        userInsert();
        postInsert();
    }

    private void createUserTable() {
        String createSql = """
            DROP TABLE IF EXISTS posts;
            DROP TABLE IF EXISTS users;
            
            CREATE TABLE IF NOT EXISTS users(
                id          UUID PRIMARY KEY,
                login       VARCHAR(64) NOT NULL,
                password    VARCHAR(128) NOT NULL,
                age         INT NOT NULL
            );
            """;
        jdbcTemplate.execute(createSql);
    }

    private void createPostTable() {
/*        String createSql = """
            DROP TABLE IF EXISTS posts;
            CREATE TABLE IF NOT EXISTS posts(
                id              UUID PRIMARY KEY,
                title           VARCHAR(128) NOT NULL,
                body            TEXT NOT NULL,
                published_at    TIMESTAMP NULL,
                created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Use CURRENT_TIMESTAMP for automatic date/time
                updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  -- Use ON UPDATE CURRENT_TIMESTAMP
            )
            """;*/
        String createSql = """
            CREATE TABLE IF NOT EXISTS posts(
                id              UUID PRIMARY KEY,
                user_id         UUID,
                title           VARCHAR(128) NOT NULL,
                body            TEXT NOT NULL,
                published_at    TIMESTAMP NULL,
                created_at      TIMESTAMP NOT NULL,
                updated_at      TIMESTAMP NOT NULL,
                FOREIGN KEY(user_id) REFERENCES users(id)
            );
            """;
        jdbcTemplate.execute(createSql);
    }

    private void userInsert() {
        String insertSql = "INSERT INTO users(id, login, password, age) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(insertSql, author1,"boklah", "tro-gernamy-228", 17);
        jdbcTemplate.update(insertSql, author2,"geletiy", "password", 17);
        jdbcTemplate.update(insertSql, UUID.randomUUID(),"romanko", "qwerty", 17);
        jdbcTemplate.update(insertSql, UUID.randomUUID(),"andrusyk", "aifan777", 17);
        jdbcTemplate.update(insertSql, UUID.randomUUID(),"lyamceva", "scoro_v_obshagu", 17);
    }

    private void postInsert() {
        String insertSql = "INSERT INTO posts(id, user_id, title, body, published_at, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        LocalDateTime now = LocalDateTime.now();
        jdbcTemplate.update(insertSql, UUID.randomUUID(), author1, "title1", "body1", now, now, now);
        jdbcTemplate.update(insertSql, UUID.randomUUID(), author1, "title2", "body2", null, now, now);
        jdbcTemplate.update(insertSql, UUID.randomUUID(), author2, "title3", "body3", now, now, now);
    }
}
