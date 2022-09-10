package com.revature.ers.auth;

import com.revature.ers.common.datasource.exceptions.InvalidRequestException;
import com.revature.ers.users.User;
import com.revature.ers.users.UserDAO;
import com.revature.ers.users.UserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.naming.AuthenticationException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    AuthService sut; // System under test (the thing being tested)
    UserDAO mockUserDAO;

    @BeforeEach
    public void setup() {
        mockUserDAO = Mockito.mock(UserDAO.class);
        sut = new AuthService(mockUserDAO);
    }

    @AfterEach
    public void cleanUp() {
        Mockito.reset(mockUserDAO); // Helps to ensure that and when/then on this mock our reset/invalidated
    }

    @Test
    public void test_Authenticate_returnsSuccessfully_givenValidAndKnownCredentials() {

        // Arrange
        Credentials credentialsStub = new Credentials("valid", "credentials");
        User userStub = new User("some-id", "Val", "id", "valid123@revature.com", "valid", "credentials", "25000");
        when(mockUserDAO.findUserByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.of(userStub));
        UserResponse expectedResult = new UserResponse(userStub);

        // Act
        UserResponse actualResult = sut.authenticate(credentialsStub);

        // Assert
        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult); // I went ahead and put my .equals method in there
        verify(mockUserDAO, times(1)).findUserByUsernameAndPassword(anyString(), anyString());

    }

    @Test
    public void test_authenticate_throwsInvalidRequestException_givenToShortOfUsername() {

        // Arrange
        Credentials credentialsStub = new Credentials("x", "p4$$2WORD");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> sut.authenticate(credentialsStub));

        verify(mockUserDAO, times(0)).findUserByUsernameAndPassword(anyString(), anyString());

    }

    @Test
    public void test_authenticate_throwsInvalidRequestException_givenToShortOfPassword() {

        // Arrange
        Credentials credentialsStub = new Credentials("invalid", "creds");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> sut.authenticate(credentialsStub));

        verify(mockUserDAO, times(0)).findUserByUsernameAndPassword(anyString(), anyString());

    }

    @Test
    public void test_authenticate_throwsInvalidRequestException_givenNullCredentials() {

        // Arrange
        Credentials credentialsStub = null;

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> sut.authenticate(credentialsStub));

        verify(mockUserDAO, times(0)).findUserByUsernameAndPassword(anyString(), anyString());

    }

    // TODO: Won't be able to perform until all Integers are changed to strings
    /*@Test
    public void test_authenticate_throwsInvalidRequestException_givenInvalidCredentials() {
        // Arrange
        Credentials credentialsStub = new Credentials("unknown", "credentials");
        /*User userStub = new User("some-id", "Val", "id", "valid123@revature.com", "valid", "credentials", "25000");
        when(mockUserDAO.findUserByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.empty());
        UserResponse expectedResult = new UserResponse(userStub);

        // Act
        assertThrows(AuthenticationException.class, () -> {
            sut.authenticate(credentialsStub);
        });

        // Assert
        verify(mockUserDAO, times(1)).findUserByUsernameAndPassword(anyString(), anyString());
    }*/

}
