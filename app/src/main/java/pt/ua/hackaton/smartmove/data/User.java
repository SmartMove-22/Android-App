package pt.ua.hackaton.smartmove.data;

public class User {

    private final String username;
    private final String email;
    private final String password;
    private final String image;

    public User(String username, String email, String password, String image) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getImage() {
        return image;
    }

}
