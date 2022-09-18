package com.revature.ers.reimbursement;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

public class UpdateRemb {

    private String reimb_id;
    private float amount;
    private Timestamp resolved;
    private String resolved_id;
    private String status_id;

    public String getReimb_id() {
        return reimb_id;
    }

    public void setReimb_id(String reimb_id) {
        this.reimb_id = reimb_id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Timestamp getResolved() {
        return resolved;
    }

    public void setResolved(Timestamp resolved) {
        this.resolved = resolved;
    }

    public String getResolved_id() {
        return resolved_id;
    }

    public void setResolved_id(String resolved_id) {
        this.resolved_id = resolved_id;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateRemb that = (UpdateRemb) o;
        return Float.compare(that.amount, amount) == 0 && Objects.equals(reimb_id, that.reimb_id) && Objects.equals(resolved, that.resolved) && Objects.equals(resolved_id, that.resolved_id) && Objects.equals(status_id, that.status_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reimb_id, amount, resolved, resolved_id, status_id);
    }

    @Override
    public String toString() {
        return "UpdateRemb{" +
                "reimb_id='" + reimb_id + '\'' +
                ", amount=" + amount +
                ", resolved=" + resolved +
                ", resolved_id='" + resolved_id + '\'' +
                ", status_id='" + status_id + '\'' +
                '}';
    }

    public Reimbursements extractEntity() {
        Reimbursements extractEntity = new Reimbursements();
        extractEntity.setAmount(this.amount);
        extractEntity.setResolved(Timestamp.valueOf(LocalDateTime.now()));
        extractEntity.setResolver_id(this.resolved_id);
        extractEntity.setStatus_id("1");
        return extractEntity;

    }
}
