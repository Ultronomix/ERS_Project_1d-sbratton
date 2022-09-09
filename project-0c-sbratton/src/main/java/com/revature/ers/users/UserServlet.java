package com.revature.ers.users;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ers.common.AppUtils;
import com.revature.ers.common.ResourceCreationResponse;
import com.revature.ers.common.datasource.exceptions.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServlet extends HttpServlet {

    private final UserService userService;

    // TODO Inject a shared reference to a configured ObjectMapper

    public UserServlet(UserService userService) {

        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        HttpSession userSession = req.getSession(false);

        // If userSession is null, this means that the requester is not authenticated with the server
        if (userSession /*!=*/ == null) {
            // TODO Encapsulate air response creation into its own utility method
            resp.setStatus(401);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 401);
            errorResponse.put("message", "Requester is not authenticated with the system, please login.");
            errorResponse.put("timestamp", System.currentTimeMillis());
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
            return;
        }

        try {

            String idToSearchFor = req.getParameter("id");
            UserResponse requester = (UserResponse) userSession.getAttribute("authUser");

            System.out.println("request is admin: " + requesterIsAdmin(requester));
            System.out.println("requester owns requested resource: " + requesterOwned(idToSearchFor, requester.getId()));

            if (!requesterIsAdmin(requester) && !requesterOwned(idToSearchFor, requester.getId())) {

                // TODO Encapsulate air response creation into its own utility method
                resp.setStatus(403); // Forbidden the system recognizes the user, but they don't have permission to be here
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("statusCode", 403);
                errorResponse.put("message", "Requester is not Permitted to communicate with this endpoint.");
                errorResponse.put("timestamp", System.currentTimeMillis());
                resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
                return;
            }

            if (idToSearchFor == null) {
                List<UserResponse> allUsers = userService.getAllUsers();
                resp.getWriter().write(jsonMapper.writeValueAsString(allUsers));
            } else {
                UserResponse foundUser = userService.getUserById(idToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundUser));
            }

        } catch (InvalidRequestException | JsonMappingException e) {

            // TODO Encapsulate air response creation into its own utility method
            resp.setStatus(400); // Bad request
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 400);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
        } catch (ResourceNotFoundException e) {

            resp.setStatus(404); // Not found: the site resource could not be located.
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 404);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
        } catch (DataSourceException e) {

            resp.setStatus(500); // Internal server error: Typically sent back when login fails or if a protected endpoint is hit by unauthorized user
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 500);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
        }
    }
    //
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        try {
            NewUserRequest requestBody =jsonMapper.readValue(req.getInputStream(), NewUserRequest.class);
            ResourceCreationResponse responseBody;
            responseBody = userService.register(requestBody);
            resp.getWriter().write(jsonMapper.writeValueAsString(responseBody));
        } catch (InvalidRequestException | JsonMappingException e) {

            // TODO Encapsulate air response creation into its own utility method
            resp.setStatus(400); // Bad request
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 400);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis()/*LocalDateTime.now()*/);
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
        } catch (ResourcePersistenceException e) {

            resp.setStatus(409); // Conflict: Indicates that the provided resource could not be saved without conflicting with other data
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 409);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis()/*LocalDateTime.now()*/);
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
        } catch (DataSourceException e) {

            resp.setStatus(500); // Internal server error: Typically sent back when login fails or if a protected endpoint is hit by unauthorized user
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 500);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis()/*LocalDateTime.now()*/);
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
        }
    }

    public boolean requesterIsAdmin(UserResponse requester) {
        return requester.getEmail().equals("pau234@revature.com"); // || requester.getEmail().equals("kam789@revature.com");
    }

    public boolean requesterOwned(String resourceId, Integer requesterId) {
        if (resourceId == null) return false;
        return requesterId.equals(AppUtils.parseInt(resourceId));
    }
}
