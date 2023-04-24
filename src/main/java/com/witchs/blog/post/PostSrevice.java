package com.witchs.blog.post;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PostSrevice {
    private final PostRepository postRepository;
    public List<Post> getNextTen(Long firstId) {
        List<Post> tenPosts = new ArrayList<Post>();
        for(Long i = firstId; i < firstId + 10; i++) {
            var post = postRepository.findById(i);
            if(post.isPresent()) {
                tenPosts.add(post.get());
            }
        }
        return tenPosts;
    }
}
