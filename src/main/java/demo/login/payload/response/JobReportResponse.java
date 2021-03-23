package demo.login.payload.response;

public class JobReportResponse {
    
    private Long reportId;
    private String message;
    private UserResponse user;
    private JobResponse job;

    public Long getReportId() {
        return reportId;
    }
    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public UserResponse getUser() {
        return user;
    }
    public void setUser(UserResponse user) {
        this.user = user;
    }
    public JobResponse getJob() {
        return job;
    }
    public void setJob(JobResponse job) {
        this.job = job;
    }

    
}
