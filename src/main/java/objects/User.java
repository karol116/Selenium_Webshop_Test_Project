package objects;

import utils.FakerUtils;

public class User {
    private String username;
    private String password;
    private String email;

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public User(){}
    public static User getDefaultUser(){
        String userName = "testUserName" + new FakerUtils().generateRandomNumber();
        return new User()
                .setUsername(userName)
                .setEmail(userName + "@mail.com")
                .setPassword("stronPass3");
    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }
}