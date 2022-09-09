package com.revature.ers.users;

import java.io.Serializable;
import java.util.Objects;

// example of a response DTO
public class UserResponse implements Serializable {

    private String id;
    private String given_name;
    private String surname;
    private String email;
    private String username;

    public  UserResponse(User subject) {

        this.id = subject.getId();
        this.given_name = subject.getGiven_name();
        this.surname = subject.getSurname();
        this.email = subject.getEmail();
        this.username = subject.getUsername();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponse that = (UserResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(given_name, that.given_name) && Objects.equals(surname, that.surname) && Objects.equals(email, that.email) && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, given_name, surname, email, username);
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", given_name='" + given_name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
