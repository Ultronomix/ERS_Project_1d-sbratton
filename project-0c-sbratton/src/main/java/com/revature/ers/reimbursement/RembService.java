package com.revature.ers.reimbursement;

import com.revature.ers.common.AppUtils;
import com.revature.ers.common.ResourceCreationResponse;
import com.revature.ers.common.datasource.exceptions.InvalidRequestException;
import com.revature.ers.common.datasource.exceptions.ResourceNotFoundException;
import com.revature.ers.common.datasource.exceptions.ResourcePersistenceException;
import com.revature.ers.users.NewUserRequest;
import com.revature.ers.users.User;

import java.util.List;
import java.util.stream.Collectors;

public class RembService {

    private final RembDAO rembDAO;

    public RembService(RembDAO rembDAO) {
        this.rembDAO = rembDAO;
    }

    public List<RembResponse> getAllReimbursements() {

        List<RembResponse> responses =  rembDAO.getAllReimbursements()
                .stream()
                .map(RembResponse::new)
                .collect(Collectors.toList());

        System.out.println("Reimbursements in Service: " + responses);

        return responses;
    }

    public RembResponse getReimbursementById(String idStr) {
        Integer reimb_id = AppUtils.parseInt(idStr);

        if (reimb_id <= 0) {
            throw new InvalidRequestException("An invalid non-positive Reimbursement ID was provided");
        }

        return rembDAO.findReimbursementsById(String.valueOf(reimb_id))
                .map(RembResponse::new)
                .orElseThrow(ResourceNotFoundException::new);

    }

    public RembResponse findReimbursementsByStatusId(String idStr) {
        Integer status_id = AppUtils.parseInt(idStr);

        if (status_id <= 0) {
            throw new InvalidRequestException("An invalid non-positive Reimbursement ID was provided");
        }

        return rembDAO.findReimbursementsById(String.valueOf(status_id))
                .map(RembResponse::new)
                .orElseThrow(ResourceNotFoundException::new);

    }

    public ResourceCreationResponse register(NewRembRequest newRembRequest) {

        if (newRembRequest == null) {
            throw new InvalidRequestException("Provided request was empty");
        }

        if (newRembRequest.getAmount() == null || newRembRequest.getAmount() <= 0) {
            throw new InvalidRequestException("A non-empty Amount must be provided");
        }

        if (newRembRequest.getDescription() == null || newRembRequest.getDescription().length() <= 0) {
            throw new InvalidRequestException("A non-empty Description must be provided");
        }

        if (newRembRequest.getAuthor_id() == null || newRembRequest.getAuthor_id().length() <= 0) {
            throw new InvalidRequestException("Your User_Id must be provided");
        }

        if (newRembRequest.getType_id() == null || newRembRequest.getType_id().length() <= 0) {
            throw new InvalidRequestException("Your type Id must either be '10' for Lodging expenses, '11' for Travel expenses" +
                    "'12' for Food expenses, or '13' for other expenses ");
        }

        Reimbursements rembToPersist = newRembRequest.extractEntity();
        String newRembId = rembDAO.save(rembToPersist);
        return new ResourceCreationResponse(newRembId);

    }

}
