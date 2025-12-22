package com.example.User;

public class CreateUser {

    private String username;
    private String password;
    private int age;
    private String repeatPassword;

    public CreateUser(String username, String password, int age, String repeatPassword) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.repeatPassword = repeatPassword;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}
