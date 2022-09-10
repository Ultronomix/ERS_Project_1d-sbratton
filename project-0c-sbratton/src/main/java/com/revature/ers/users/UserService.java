package com.revature.ers.users;

import com.revature.ers.common.AppUtils;
import com.revature.ers.common.ResourceCreationResponse;
import com.revature.ers.common.datasource.exceptions.InvalidRequestException;
import com.revature.ers.common.datasource.exceptions.ResourceNotFoundException;
import com.revature.ers.common.datasource.exceptions.ResourcePersistenceException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public  List<UserResponse> getAllUsers() {

        // Functional approach (more declarative)
        return userDAO.getAllUsers()
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());

    }

    //
    public UserResponse getUserById(String idStr) {

        int id = AppUtils.parseInt(idStr);

        if (id <= 0) {
            throw new InvalidRequestException("A invalid (non-positive) id was provided!");
        }

        return userDAO.findUserById(id)
                .map(UserResponse::new)
                .orElseThrow(ResourceNotFoundException::new);

    }

    //
    public ResourceCreationResponse register(NewUserRequest newUserRequest) {

        if (newUserRequest == null) {
            throw new InvalidRequestException("Provided request was empty");
        }

        if (newUserRequest.getGiven_name() == null || newUserRequest.getGiven_name().length() <= 0 || newUserRequest.getSurname() == null
                || newUserRequest.getSurname().length() <= 0) {
            throw new InvalidRequestException("A non-empty given name and surname must be provided");
        }

        if (newUserRequest.getEmail() == null || newUserRequest.getEmail().length() <= 0) {
            throw new InvalidRequestException("A non-empty email must be provided");
        }

        if (newUserRequest.getUsername() == null || newUserRequest.getUsername().length() < 4) {
            throw new InvalidRequestException("Username must be at least four characters");
        }

        if (newUserRequest.getPassword() == null || newUserRequest.getPassword().length() < 8) {
            throw new InvalidRequestException("Password must be at least 8 characters");
        }
        if (userDAO.isEmailTaken(newUserRequest.getEmail())) {
            throw new ResourcePersistenceException("The provided email is already taken");
        }

        if (userDAO.isUsernameTaken(newUserRequest.getUsername())) {
            throw new ResourcePersistenceException("The provided username is already taken");
        }

        User userToPersist = newUserRequest.extractEntity();
        String newUserId = userDAO.save(userToPersist);
        return new ResourceCreationResponse(newUserId);

    }

}
