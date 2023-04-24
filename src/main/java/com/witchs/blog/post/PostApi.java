package com.witchs.blog.post;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
public class PostApi {
    private final PostRepository postRepository;
    private final PostSrevice postSrevice;

    @GetMapping("/posts")
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @GetMapping("/getNextPosts")
    public List<Post> getNextPost(@RequestBody NextPostRequest nextPostRequest) {
        return postSrevice.getNextTen(nextPostRequest.getFirstId());
    }

    @PostMapping("/create/post")
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest) {
        Post post = new Post(postRequest.getTitle(), 0, 0, postRequest.getContent(), new Date(), postRequest.getUserId());
        postRepository.save(post);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
