package pt.ua.hackaton.smartmove.data;

public class LoginResponse {

    private String message;
    private String code;
    private String Authorization;

    public LoginResponse() {};

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAuthorization() {
        return Authorization;
    }

    public void setAuthorization(String authorization) {
        Authorization = authorization;
    }
}
