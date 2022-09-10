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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class AuthServlet extends HttpServlet {

    private static Logger logger = LogManager.getLogger(AuthServlet.class);
    //private final UserDAO userDAO;

    private final AuthService authService;

    public AuthServlet(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        logger.info("A POST What is received by /ers/ at {}", LocalDateTime.now());

        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        try {
            Credentials credentials = jsonMapper.readValue(req.getInputStream(), Credentials.class);
            UserResponse responseBody = authService.authenticate(credentials);
            resp.setStatus(200); // Ok: technically this is the default

            // Establishes on http://session that is implicitly attached to the response as a cookie
            // The web client will automatically attach this cookie to subsequent request to the server
            logger.info("Establishing user session for user: {}", responseBody.getUsername());
            HttpSession userSession = req.getSession();
            userSession.setAttribute("authUser", responseBody);

            resp.getWriter().write(jsonMapper.writeValueAsString(responseBody));

            logger.info("POST Request successfully processed at {}", LocalDateTime.now());

        } catch (InvalidRequestException | JsonMappingException e) {
            logger.warn("Error processing request at {}, Error message: {}", LocalDateTime.now(), e.getMessage());

            // TODO Encapsulate air response creation into its own utility method
            resp.setStatus(400); // Bad request
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));

        } catch (AuthenticationException e) {
            logger.warn("Failed login at {}, Error message: {}", LocalDateTime.now(), e.getMessage());

            resp.setStatus(401); // Unauthorized: Typically sent back when login fails or if a protected endpoint is hit by unauthorized user
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, e.getMessage())));

        } catch (DataSourceException e) {
            logger.error("A data source error occurred at {}, Error message: {}", LocalDateTime.now(), e.getMessage());

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
