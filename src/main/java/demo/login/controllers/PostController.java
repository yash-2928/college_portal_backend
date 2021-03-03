package demo.login.controllers;

import demo.login.payload.response.PostResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.login.data.Post;
import demo.login.data.User;
import demo.login.payload.request.PostRequest;
import demo.login.repository.PostRepository;
import demo.login.repository.UserRepository;

@RestController
@RequestMapping(path = "/api")
@CrossOrigin(origins = "*")
public class PostController {
    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    private PostResponse mapPostToPostResponse(Post post) {
        PostResponse postResponse = new PostResponse();
        postResponse.setPostId(post.getPostId());
        postResponse.setContent(post.getContent());
        postResponse.setPostDate(post.getPostDate());
        postResponse.setUser(post.getUser());
        postResponse.setPostTitle(post.getPostTitle());
        postResponse.setPostType(post.getPostType());
        postResponse.setReported(post.getReported());
        postResponse.setComments(post.getComments());
        return postResponse;
    }

    @PostMapping("/post")
    public ResponseEntity<String> uplaodPost(@RequestBody PostRequest postRequest) throws IOException {
        User user = userRepository.findById(postRequest.getUserId()).get();
        Post post = new Post(user, postRequest.getPostTitle(), postRequest.getContent(), postRequest.getPostType());
        postRepository.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/posts")
    public List<PostResponse> getPosts() {
        List<Post> posts = postRepository.findAll();
        posts.sort((a, b) -> b.getPostDate().compareTo(a.getPostDate()));
        return posts.stream().map(post -> mapPostToPostResponse(post)).collect(Collectors.toList());
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            return ResponseEntity.ok(mapPostToPostResponse(postOptional.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        try {
            postRepository.deleteById(id);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
