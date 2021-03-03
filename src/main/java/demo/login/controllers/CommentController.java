package demo.login.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import demo.login.data.Comment;
import demo.login.data.Post;
import demo.login.data.User;
import demo.login.payload.request.CommentRequest;
import demo.login.payload.response.MessageResponse;
import demo.login.repository.CommentRepository;
import demo.login.repository.PostRepository;
import demo.login.repository.UserRepository;

@RestController
public class CommentController {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @PostMapping("/comment")
    public ResponseEntity<MessageResponse> postComment(@RequestBody CommentRequest commentRequest) {
        User user = userRepository.findById(commentRequest.getUserId()).get();
        Post post = postRepository.findById(commentRequest.getPostId()).get();
        Comment comment = new Comment(user, post, commentRequest.getCommentContent());
        commentRepository.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        try {
            commentRepository.deleteById(id);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
