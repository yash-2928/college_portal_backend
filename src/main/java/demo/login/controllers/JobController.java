package demo.login.controllers;

import java.io.IOException;
import java.security.InvalidKeyException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import demo.login.data.Job;
import demo.login.data.User;
import demo.login.payload.response.JobResponse;
import demo.login.repository.BlobStorageRepository;
import demo.login.repository.JobRepository;
import demo.login.repository.UserRepository;

@RestController
@RequestMapping(path = "/api")
@CrossOrigin(origins = "*")
public class JobController {
    
    @Autowired
    JobRepository jobRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BlobStorageRepository blobStorageRepository;

    private JobResponse mapJobToJobResponse(Job job) {
        JobResponse jobResponse = new JobResponse();
        jobResponse.setJobId(job.getJobId());
        jobResponse.setContent(job.getContent());
        jobResponse.setJobDate(job.getJobDate());
        jobResponse.setUser(job.getUser());
        jobResponse.setJobTitle(job.getJobTitle());
        jobResponse.setLink(job.getLink());
        jobResponse.setCompanyName(job.getCompanyName());
        jobResponse.setReported(job.getReported());
        jobResponse.setComments(job.getComments());
        jobResponse.setFileUrl(job.getFileUrl());
        return jobResponse;
    }

    // @PostMapping("/post")
    // public ResponseEntity<String> uplaodPost(@RequestBody PostRequest
    // postRequest) throws IOException {
    // User user = userRepository.findById(postRequest.getUserId()).get();
    // Post post = new Post(user, postRequest.getPostTitle(),
    // postRequest.getContent(), postRequest.getPostType());
    // postRepository.save(post);
    // return ResponseEntity.status(HttpStatus.CREATED).build();
    // }

    @PostMapping("/job")
    public ResponseEntity<String> testUpload(@RequestParam("userId") Long userId,
            @RequestParam("jobTitle") String jobTitle, @RequestParam("jobContent") String jobContent,
            @RequestParam("link") String link, @RequestParam("companyName") String companyName,
            @RequestParam("file") MultipartFile file) {
        String fileType = file.getContentType();
        String filename = file.getOriginalFilename();
        try {
            byte[] fileBytes = file.getBytes();
            String fileUrl = blobStorageRepository.uploadFile("postdocuments", filename, fileBytes);
            User user = userRepository.findById(userId).get();
            Job job = new Job(user, jobTitle, jobContent, link, companyName, fileType, fileUrl);
            jobRepository.save(job);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IOException | InvalidKeyException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/jobs")
    public List<JobResponse> getJobs() {
        List<Job> jobs = jobRepository.findAll();
        jobs.sort((a, b) -> b.getJobDate().compareTo(a.getJobDate()));
        return jobs.stream().map(Job -> mapJobToJobResponse(Job)).collect(Collectors.toList());
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<JobResponse> getJob(@PathVariable Long id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            return ResponseEntity.ok(mapJobToJobResponse(jobOptional.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        try {
            jobRepository.deleteById(id);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

