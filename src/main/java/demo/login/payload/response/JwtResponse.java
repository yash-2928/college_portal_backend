package demo.login.payload.response;

public class JwtResponse {

    private String accessToken;
    private String type = "Bearer";
    private String email;
    private Long userId;
    private Boolean isAdmin;

    public JwtResponse(String accessToken, String email, Long userId, Boolean isAdmin) {
        this.accessToken = accessToken;
        this.email = email;
        this.userId = userId;
        this.isAdmin = isAdmin;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
