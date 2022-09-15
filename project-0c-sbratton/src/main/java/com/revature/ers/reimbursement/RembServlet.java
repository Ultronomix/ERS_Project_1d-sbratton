package com.revature.ers.reimbursement;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ers.common.ErrorResponse;
import com.revature.ers.common.ResourceCreationResponse;
import com.revature.ers.common.datasource.exceptions.DataSourceException;
import com.revature.ers.common.datasource.exceptions.InvalidRequestException;
import com.revature.ers.common.datasource.exceptions.ResourceNotFoundException;
import com.revature.ers.common.datasource.exceptions.ResourcePersistenceException;
import com.revature.ers.users.UserResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

public class RembServlet extends HttpServlet {

    private final RembService rembService;

    public RembServlet(RembService rembService) {
        this.rembService = rembService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        // Access the HTTP session on the request (if it exists: otherwise it will be null)
        HttpSession userSession = req.getSession(false);

        // If userSession is null, this means that the requester is not authenticated with the server
        if (userSession == null) {
            resp.setStatus(401);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401,
                    "Requester is not authenticated with the system, please login.")));
            return;
        }

        try {

            String idToSearchFor = req.getParameter("id");
            UserResponse requester = (UserResponse) userSession.getAttribute("authUser");

            System.out.println("request is admin: " + requesterIsAdmin(requester));
            System.out.println("requester owns requested resource: " + requesterOwned(idToSearchFor, requester.getUser_id()));

//            if (!requesterIsAdmin(requester) && !requesterOwned(idToSearchFor, requester.getUser_id())) {
//                resp.setStatus(403); // Forbidden the system recognizes the user, but they don't have permission to be here
//                resp.getWriter().write(jsonMapper.writeValueAsString(
//                        /*errorResponse*/new ErrorResponse(403,
//                                "Requester is not Permitted to communicate with this endpoint.")));
//                return;
//            }

            if (idToSearchFor == null) {
                List<RembResponse> allUsers = rembService.getAllReimbursements();
                resp.getWriter().write(jsonMapper.writeValueAsString(allUsers));
            } else {
                RembResponse foundUser = rembService.getReimbursementById(idToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundUser));
            }

        } catch (InvalidRequestException | JsonMappingException e) {
            resp.setStatus(400); // Bad request
            resp.getWriter().write(jsonMapper.writeValueAsString(/*errorResponse*/new ErrorResponse(400, e.getMessage())));

        } catch (ResourceNotFoundException e) {

            resp.setStatus(404); // Not found: the site resource could not be located.
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(404, e.getMessage())));

        } catch (DataSourceException e) {

            resp.setStatus(500); // Internal server error: Typically sent back when login fails or if a protected endpoint is hit by unauthorized user
            resp.getWriter().write(jsonMapper.writeValueAsString(/*errorResponse*/new ErrorResponse(500, e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        try {
            NewRembRequest requestBody = jsonMapper.readValue(req.getInputStream(), NewRembRequest.class);
            ResourceCreationResponse responseBody;
            responseBody = (ResourceCreationResponse) rembService.getAllReimbursements();
            resp.getWriter().write(jsonMapper.writeValueAsString(responseBody));
        } catch (InvalidRequestException | JsonMappingException e) {
            resp.setStatus(400); // Bad request
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));

        } catch (ResourcePersistenceException e) {

            resp.setStatus(409); // Conflict: Indicates that the provided resource could not be saved without conflicting with other data
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(409, e.getMessage())));

        } catch (DataSourceException e) {

            resp.setStatus(500); // Internal server error: Typically sent back when login fails or if a protected endpoint is hit by unauthorized user
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
        }
    }

    private boolean requesterIsAdmin(UserResponse requester) {
        return requester.getUser_id().equals("00050");
    }

    private boolean requesterOwned(String resourceId, String requesterId) {
        if (resourceId == null) return false;
        return requesterId.equals(resourceId);
    }

}
