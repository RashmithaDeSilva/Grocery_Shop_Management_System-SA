package model;

public class User {
    private int userId;
    private String userName;
    private String email;
    private String password;
    private int title;


    public User() {}

    public User(int userId) {
        this.userId = userId;
    }

    public User(int userId, String userName, String email, String password) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public User(int userId, String userName, String email, int title) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.title = title;
    }

    public User(String userName, String email, String password, int title) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.title = title;
    }

    public User(int userId, String userName, String email, String password, int title) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.title = title;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }
}
