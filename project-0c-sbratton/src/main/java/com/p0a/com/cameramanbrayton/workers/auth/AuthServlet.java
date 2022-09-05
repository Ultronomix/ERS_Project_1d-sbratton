package main.java.com.p0a.com.cameramanbrayton.workers.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.com.p0a.com.cameramanbrayton.workers.users.User;
import main.java.com.p0a.com.cameramanbrayton.workers.users.UserDAO;

import java.io.IOException;

public class AuthServlet extends HttpServlet {

    private final UserDAO userDAO;

    public AuthServlet(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doPost(req, resp);
        //resp.getWriter().write("/auth works!");
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");
        Credentials credentials = jsonMapper.readValue(req.getInputStream(), Credentials.class);
        User loggedInUser  = userDAO.findUserByUsernameAndPassword(credentials.getUsername(), credentials.getPassword())
                .orElseThrow(() -> new RuntimeException("No user found with those Credentials!"));

        resp.getWriter().write(jsonMapper.writeValueAsString(loggedInUser));
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }
}
