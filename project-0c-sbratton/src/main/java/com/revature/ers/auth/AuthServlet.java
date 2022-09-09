package com.revature.ers.auth;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ers.common.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.revature.ers.common.datasource.exceptions.AuthenticationException;
import com.revature.ers.common.datasource.exceptions.DataSourceException;
import com.revature.ers.common.datasource.exceptions.InvalidRequestException;
import com.revature.ers.users.UserResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthServlet extends HttpServlet {

    //private final UserDAO userDAO;

    private final AuthService authService;

    public AuthServlet(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        try {
            Credentials credentials = jsonMapper.readValue(req.getInputStream(), Credentials.class);
            UserResponse responseBody = authService.authenticate(credentials);
            resp.setStatus(200); // Ok: technically this is the default

            // Establishes on http://session that is implicitly attached to the response as a cookie
            // The web client will automatically attach this cookie to subsequent request to the server
            HttpSession userSession = req.getSession();
            userSession.setAttribute("authUser", responseBody);

            resp.getWriter().write(jsonMapper.writeValueAsString(responseBody));

        } catch (InvalidRequestException | JsonMappingException e) {

            // TODO Encapsulate air response creation into its own utility method
            resp.setStatus(400); // Bad request
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));

        } catch (AuthenticationException e) {

            resp.setStatus(401); // Unauthorized: Typically sent back when login fails or if a protected endpoint is hit by unauthorized user
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, e.getMessage())));

        } catch (DataSourceException e) {

            resp.setStatus(500); // Internal server error: Typically sent back when login fails or if a protected endpoint is hit by unauthorized user
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
        }
    }

    // Logout
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate(); // This effectively logs out the requester by invalidating the session within the server
    }
}
