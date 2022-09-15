package com.revature.ers.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.revature.ers.common.Request;

import java.util.Objects;

public class NewUserRequest implements Request<User> {

    private String username;
    @JsonProperty("username")
    private String email;
    @JsonProperty("email")
    private String password;
    @JsonProperty("password")
    private String given_name;
    @JsonProperty("given_name")
    private String surname;
    @JsonProperty("surname")
    private String is_active;

    public NewUserRequest(String username, String email, String password, String given_name, String surname, String is_active) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.given_name = given_name;
        this.surname = surname;
        this.is_active = is_active;
    }

    @JsonProperty("is_active")



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewUserRequest that = (NewUserRequest) o;
        return Objects.equals(username, that.username) && Objects.equals(email, that.email)
                && Objects.equals(password, that.password) && Objects.equals(given_name, that.given_name)
                && Objects.equals(surname, that.surname) && Objects.equals(is_active, that.is_active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, password, given_name, surname, is_active);
    }

    @Override
    public String toString() {
        return "NewUserRequest{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", given_name='" + given_name + '\'' +
                ", surname='" + surname + '\'' +
                ", is_active='" + is_active + '\'' +
                '}';
    }

    @Override
    public User extractEntity() {
        User extractEntity = new User();
        extractEntity.setUsername(this.username);
        extractEntity.setEmail(this.email);
        extractEntity.setPassword(this.password);
        extractEntity.setGiven_name(this.given_name);
        extractEntity.setSurname(this.surname);
        extractEntity.setIs_active(this.is_active.isEmpty());
        return extractEntity;

    }
}
