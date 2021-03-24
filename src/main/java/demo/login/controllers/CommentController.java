package demo.login.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import demo.login.data.Comment;
import demo.login.data.Job;
import demo.login.data.Post;
import demo.login.data.User;
import demo.login.payload.request.CommentRequest;
import demo.login.payload.request.JobCommentRequest;
import demo.login.payload.response.MessageResponse;
import demo.login.repository.CommentRepository;
import demo.login.repository.JobRepository;
import demo.login.repository.PostRepository;
import demo.login.repository.UserRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CommentController {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    JobRepository jobRepository;

    @PostMapping("/comment")
    public ResponseEntity<MessageResponse> postComment(@RequestBody CommentRequest commentRequest) {
        User user = userRepository.findById(commentRequest.getUserId()).get();
        Comment comment = new Comment();
        comment.setUser(user);
        if (commentRequest.getPostId() != null) {
            Post post = postRepository.findById(commentRequest.getPostId()).get();
            comment.setPost(post);
        }
        if (commentRequest.getJobId() != null) {
            Job job = jobRepository.findById(commentRequest.getJobId()).get();
            comment.setJob(job);
        }
        comment.setCommentContent(commentRequest.getCommentContent());
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
