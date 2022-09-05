package main.java.com.p0a.com.cameramanbrayton.workers.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        resp.getWriter().write("/auth works!");
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }
}
