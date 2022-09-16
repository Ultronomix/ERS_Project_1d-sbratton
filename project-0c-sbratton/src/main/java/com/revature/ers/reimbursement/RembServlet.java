package com.revature.ers.reimbursement;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ers.common.ErrorResponse;
import com.revature.ers.common.ResourceCreationResponse;
import com.revature.ers.common.datasource.exceptions.DataSourceException;
import com.revature.ers.common.datasource.exceptions.InvalidRequestException;
import com.revature.ers.common.datasource.exceptions.ResourceNotFoundException;
import com.revature.ers.common.datasource.exceptions.ResourcePersistenceException;
import com.revature.ers.users.NewUserRequest;
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
            RembResponse requester = (RembResponse) userSession.getAttribute("authUser");

            if (idToSearchFor == null) {
                List<RembResponse> allReimbs = rembService.getAllReimbursements();
                System.out.println("Reimbusrements in Servlet: " + allReimbs);
                resp.getWriter().write(jsonMapper.writeValueAsString(allReimbs));
            } else {
                RembResponse foundUser = rembService.getReimbursementById(idToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundUser));
            }

        } catch (InvalidRequestException | JsonMappingException e) {
            resp.setStatus(400); // Bad request
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));

        } catch (ResourceNotFoundException e) {

            resp.setStatus(404); // Not found: the site resource could not be located.
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(404, e.getMessage())));

        } catch (DataSourceException e) {

            resp.setStatus(500); // Internal server error: Typically sent back when login fails or if a protected endpoint is hit by unauthorized user
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        try {
            NewRembRequest requestBody = jsonMapper.readValue(req.getInputStream(), NewRembRequest.class);
            ResourceCreationResponse responseBody;
            responseBody = rembService.register(requestBody);
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

}
