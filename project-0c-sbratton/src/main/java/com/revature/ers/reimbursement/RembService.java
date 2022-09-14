package com.revature.ers.reimbursement;

import com.revature.ers.common.AppUtils;
import com.revature.ers.common.ResourceCreationResponse;
import com.revature.ers.common.datasource.exceptions.InvalidRequestException;
import com.revature.ers.common.datasource.exceptions.ResourceNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

public class RembService {

    private final RembDAO rembDAO;

    public RembService(RembDAO rembDAO) {
        this.rembDAO = rembDAO;
    }

    public List<RembResponse> getAllReimbursements() {

        return rembDAO.getAllReimbursements()
                .stream()
                .map(RembResponse::new)
                .collect(Collectors.toList());
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

}
