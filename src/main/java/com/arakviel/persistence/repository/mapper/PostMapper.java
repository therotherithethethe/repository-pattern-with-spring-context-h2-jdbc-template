package com.arakviel.persistence.repository.mapper;

import com.arakviel.persistence.entity.Post;
import com.arakviel.persistence.repository.contracts.UserRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class PostMapper implements RowMapper<Post> {

    private final UserRepository userRepository;

    public PostMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Post.builder()
            .id(UUID.fromString(rs.getString("id")))
            .user(userRepository.findById(UUID.fromString(rs.getString("user_id"))).orElseThrow())
            .title(rs.getString("title"))
            .body(rs.getString("body"))
            .publishedAt(rs.getObject("published_at", LocalDateTime.class))
            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
            .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
            .build();
    }
}
