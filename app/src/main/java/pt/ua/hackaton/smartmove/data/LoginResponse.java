package pt.ua.hackaton.smartmove.data;

public class LoginResponse {

    private String message;
    private String code;
    private String authorization;
    private String type;

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
        return authorization;
    }

    public void setAuthorization(String authorization) {
        authorization = authorization;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }
}
