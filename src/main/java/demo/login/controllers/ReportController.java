package demo.login.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import demo.login.data.Post;
import demo.login.data.Report;
import demo.login.data.User;
import demo.login.payload.request.ReportRequest;
import demo.login.payload.response.MessageResponse;
import demo.login.repository.PostRepository;
import demo.login.repository.ReportRepository;
import demo.login.repository.UserRepository;

public class ReportController {
    public class CommentController {

        @Autowired
        ReportRepository reportRepository;
    
        @Autowired
        UserRepository userRepository;
    
        @Autowired
        PostRepository postRepository;
    
        @PostMapping("/report")
        public ResponseEntity<MessageResponse> postComment(@RequestBody ReportRequest reportRequest) {
            User user = userRepository.findById(reportRequest.getUserId()).get();
            Post post = postRepository.findById(reportRequest.getPostId()).get();
            Report report = new Report(user, post, reportRequest.getReportId());
            reportRepository.save(report);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
    
        @DeleteMapping("/report/{id}")
        public ResponseEntity<String> deleteComment(@PathVariable Long id) {
            try {
                reportRepository.deleteById(id);
                return ResponseEntity.accepted().build();
            } catch (Exception e) {
                return ResponseEntity.notFound().build();
            }
        }
    }
    
}
