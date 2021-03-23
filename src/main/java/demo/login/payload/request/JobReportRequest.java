package demo.login.payload.request;

import javax.validation.constraints.NotNull;

public class JobReportRequest {
    
    @NotNull
    private Long userId;
    @NotNull
    private Long jobId;
    private String message;

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getJobId() {
        return jobId;
    }
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
