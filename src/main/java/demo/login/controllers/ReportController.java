package demo.login.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

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
import demo.login.data.Report;
import demo.login.data.User;
import demo.login.payload.request.ReportRequest;
import demo.login.payload.response.MessageResponse;
import demo.login.payload.response.ReportResponse;
import demo.login.repository.PostRepository;
import demo.login.repository.ReportRepository;
import demo.login.repository.UserRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommonService commonService;

    @PostMapping("/report")
    public ResponseEntity<MessageResponse> reportPost(@Valid @RequestBody ReportRequest reportRequest) {
        User user = userRepository.findById(reportRequest.getUserId()).get();
        Post post = postRepository.findById(reportRequest.getPostId()).get();
        Report report = new Report(user, post, reportRequest.getMessage());
        reportRepository.save(report);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/reports")
    public List<ReportResponse> getReports() {
        return reportRepository.findAll().stream().map(commonService::mapReportToReportResponse)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/report/{id}")
    public ResponseEntity<String> deleteReport(@PathVariable Long id) {
        try {
            reportRepository.deleteById(id);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
