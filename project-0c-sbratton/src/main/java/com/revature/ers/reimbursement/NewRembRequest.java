package com.revature.ers.reimbursement;

import com.revature.ers.common.Request;

import java.sql.Timestamp;
import java.util.UUID;

public class NewRembRequest implements Request<Reimbursements> {

    private String reimb_id;
    private Integer amount;
    private Timestamp submitted;
    private Timestamp resolved;
    private String description;
    private String author_id;
    private String resolver_id;
    private String status_id;
    private String type_id;

    public String getReimb_id() {
        return reimb_id;
    }

    public void setReimb_id(String reimb_id) {
        this.reimb_id = reimb_id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Timestamp getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Timestamp submitted) {
        this.submitted = submitted;
    }

    public Timestamp getResolved() {
        return resolved;
    }

    public void setResolved(Timestamp resolved) {
        this.resolved = resolved;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getResolver_id() {
        return resolver_id;
    }

    public void setResolver_id(String resolver_id) {
        this.resolver_id = resolver_id;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    @Override
    public String toString() {
        return "NewRembRequest{" +
                "reimb_id='" + reimb_id + '\'' +
                ", amount=" + amount +
                ", submitted=" + submitted +
                ", resolved=" + resolved +
                ", description='" + description + '\'' +
                ", author_id='" + author_id + '\'' +
                ", resolver_id='" + resolver_id + '\'' +
                ", status_id='" + status_id + '\'' +
                ", type_id='" + type_id + '\'' +
                '}';
    }

    @Override
    public Reimbursements extractEntity() {
        Reimbursements extractEntity = new Reimbursements();
        extractEntity.setReimb_id(UUID.randomUUID().toString());
        extractEntity.setAmount(this.amount);
        extractEntity.setSubmitted(this.submitted);
        extractEntity.setResolved(this.resolved);
        extractEntity.setDescription(this.description);
        extractEntity.setAuthor_id(this.author_id);
        extractEntity.setResolver_id(this.resolver_id);
        extractEntity.setStatus_id(this.status_id);
        extractEntity.setType_id(this.type_id);
        return extractEntity;
    }

}
