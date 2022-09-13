package com.revature.ers.users;

import java.io.Serializable;
import java.util.Objects;

// example of a response DTO
public class UserResponse implements Serializable {
    private String user_id;
    private String username;
    private String email;
    private String password;
    private String given_name;
    private String surname;
    private Boolean is_active;
    private String role_id;

    public  UserResponse(User subject) {

        this.user_id = subject.getUser_id();
        this.username = subject.getUsername();
        this.email = subject.getEmail();
        this.password = subject.getPassword();
        this.given_name = subject.getGiven_name();
        this.surname = subject.getSurname();
        this.is_active = String.valueOf(subject.getIs_active()).isEmpty();
        this.role_id = subject.getRole_id();

        }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

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

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponse that = (UserResponse) o;
        return Objects.equals(user_id, that.user_id) && Objects.equals(username, that.username)
                && Objects.equals(email, that.email) && Objects.equals(password, that.password)
                && Objects.equals(given_name, that.given_name) && Objects.equals(surname, that.surname)
                && Objects.equals(is_active, that.is_active) && Objects.equals(role_id, that.role_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, username, email, password, given_name, surname, is_active, role_id);
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", given_name='" + given_name + '\'' +
                ", surname='" + surname + '\'' +
                ", is_active=" + is_active +
                ", role_id='" + role_id + '\'' +
                '}';
    }
}
