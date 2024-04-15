package com.arakviel.persistence.repository.impl;

import com.arakviel.persistence.config.ConnectionManager;
import com.arakviel.persistence.entity.Post;
import com.arakviel.persistence.repository.genericrepository.GenericRepository;
import com.arakviel.persistence.repository.contracts.PostRepository;
import com.arakviel.persistence.repository.mapper.PostMapper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class PostRepositoryImpl
    extends GenericRepository<Post>
    implements PostRepository {

    public PostRepositoryImpl(ConnectionManager connectionManager, PostMapper postMapper) {
        super(connectionManager, "posts", postMapper);
    }

    @Override
    protected List<String> tableAttributes() {
        return List.of("user_id", "title", "body", "published_at", "created_at", "updated_at");
    }

    @Override
    protected List<Object> tableValues(Post post) {
        var list = new ArrayList<>();
        list.add(post.getUser().getId());
        list.add(post.getTitle());
        list.add(post.getBody());
        list.add(post.getPublishedAt());
        return list;
    }
}
