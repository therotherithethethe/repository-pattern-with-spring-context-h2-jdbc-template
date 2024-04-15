package com.arakviel.persistence.entity;

import com.arakviel.persistence.repository.genericrepository.Column;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post implements GenericEntity {

    private UUID id;
    @Column(name = "user_id")
    private User user;
    private String title;
    private String body;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
