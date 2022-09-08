package com.revature.ers.users;

import com.revature.ers.common.Request;

public class NewUserRequest implements Request<User> {

    private String given_name;
    private String surname;
    private String email;
    private String username;
    private String password;

    public String getGiven_name() {
        return given_name;
    }

    public void setGiven_name(String given_name) {
        this.given_name = given_name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    @Override
    public String toString() {
        return "NewUserRequest{" +
                "given_name='" + given_name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }


    @Override
    public User extractEntity() {
        User extractedEntity = new User();
        extractedEntity.setGiven_name(this.given_name);
        extractedEntity.setSurname(this.surname);
        extractedEntity.setEmail(this.getEmail());
        extractedEntity.setEmail(this.getEmail());
        extractedEntity.setUsername(this.username);
        extractedEntity.setPassword(this.password);
        return extractedEntity;
    }
}
