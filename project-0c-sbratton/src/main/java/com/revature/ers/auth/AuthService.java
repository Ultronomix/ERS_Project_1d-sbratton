package com.revature.ers.auth;

import com.revature.ers.common.datasource.exceptions.AuthenticationException;
import com.revature.ers.common.datasource.exceptions.InvalidRequestException;
import com.revature.ers.users.UserDAO;
import com.revature.ers.users.UserResponse;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;

    }

    // TODO: Do not expose entity classes From the service Layer; Replace with a DTO
    public UserResponse authenticate(Credentials credentials) {

        if (credentials == null /*|| credentials.getUsername().length() < 4 || credentials.getPassword().length() < 8*/) {
            throw new InvalidRequestException("The provided credentials object was found to be null."); // TODO Replace with a custom exception
        }

        if (credentials.getUsername().length() < 4) {

            throw new InvalidRequestException("Provided username it was not the correct length (Must be at least 4 characters.)");

        }

        if (credentials.getPassword().length() < 8) {

            throw new InvalidRequestException("Provided password it was not the correct length (Must be at least 8 characters.)");

        }

        return userDAO.findUserByUsernameAndPassword(credentials.getUsername(), credentials.getPassword())
                .map(UserResponse::new)
                .orElseThrow(AuthenticationException::new);

    }

}
