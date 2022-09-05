package main.java.com.p0a.com.cameramanbrayton.workers.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class UserServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    public UserServlet(UserDAO userDAO) {

        this.userDAO = userDAO;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");
        List<User> allUsers = userDAO.getAllUsers();
        resp.getWriter().write(jsonMapper.writeValueAsString(allUsers));
    }
}
