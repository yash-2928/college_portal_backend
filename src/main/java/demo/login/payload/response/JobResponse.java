package demo.login.payload.response;

import java.util.*;
import java.util.List;

import demo.login.data.Comment;

public class JobResponse {
    private Long jobId;
    private UserResponse user;
    private String companyName;
    private String content;
    private Date jobDate;
    private Boolean reported;
    private List<Comment> comments;
    private String fileUrl;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getJobDate() {
        return jobDate;
    }

    public void setJobDate(java.util.Date date) {
        this.jobDate = date;
    }

    public Boolean getReported() {
        return reported;
    }

    public void setReported(Boolean reported) {
        this.reported = reported;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
