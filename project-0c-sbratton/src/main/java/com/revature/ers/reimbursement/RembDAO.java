package com.revature.ers.reimbursement;

import com.revature.ers.common.datasource.ConnectionFactory;
import com.revature.ers.common.datasource.exceptions.DataSourceException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RembDAO {

    private final String baseSelect = "SELECT reimb_id, amount, submitted, resolved, description, receipt, payment_id, " +
            "author_id, resolver_id, status_id, type_id " +
            "FROM ers.ers_reimbursements ";
    //private List<Reimbursements> allReimbursements;

    public List<Reimbursements> getAllReimbursements() {

        List<Reimbursements> allReimbursements = new ArrayList<>();


        try (Connection connection = ConnectionFactory.getInstance().getConnection()) {

                // JDBC Statement objects are subject to SQL Injections
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(baseSelect);

                allReimbursements = mapResultSet(resultSet);

        } catch (SQLException e) {
            System.err.println("Something went wrong when communicating with the database!");
            e.printStackTrace();
        }

        System.out.println("Reimbursements in DAO: " + allReimbursements);

        return allReimbursements;

    }

    public Optional<Reimbursements> findReimbursementsById(String reimb_id) {
        String sql = baseSelect + "WHERE reimb_id = ?";

        try {
            try (Connection connection = ConnectionFactory.getInstance().getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setObject(1, reimb_id);
                ResultSet resultSet = preparedStatement.executeQuery();
                return mapResultSet(resultSet).stream().findFirst();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DataSourceException(e);
            }
        }

    public Optional<Reimbursements> findReimbursementsByStatusId(String status_id) {
        String sql = baseSelect + "WHERE status_id = ?";

        try {
            try (Connection connection = ConnectionFactory.getInstance().getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setObject(1, status_id);
                ResultSet resultSet = preparedStatement.executeQuery();
                return mapResultSet(resultSet).stream().findFirst();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataSourceException(e);
        }
    }

        public String save(Reimbursements reimbursements) {

        String sql = "INSERT INTO ers.ers_reimbursements " +
                "(reimb_id, amount, submitted, resolved, description, author_id, resolver_id, status_id, type_id) " +
                "VALUES (?, 0, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = connection.prepareStatement(sql, new String[] {"reimb_id"});
            pstmt.setInt(1, reimbursements.getAmount());
            pstmt.setTimestamp(2, reimbursements.getSubmitted());
            pstmt.setTimestamp(3, reimbursements.getResolved());
            pstmt.setString(4, reimbursements.getDescription());
            pstmt.setString(5, reimbursements.getAuthor_id());
            pstmt.setString(6, reimbursements.getResolver_id());
            pstmt.setString(7, reimbursements.getStatus_id());
            pstmt.setString(8, reimbursements.getType_id());

        } catch (SQLException e) {
            log("ERROR", e.getMessage());
        }

            log("INFO", "Successfully persisted new reimbursement with reimbursement ID: " + reimbursements.getReimb_id());
            return reimbursements.getReimb_id();
        }
        private  List<Reimbursements> mapResultSet(ResultSet resultSet) throws  SQLException {
        List<Reimbursements> reimbursements = new ArrayList<>();
            while (resultSet.next()) {
                Reimbursements reimbursement = new Reimbursements();
                reimbursement.setReimb_id(resultSet.getString("reimb_id"));
                reimbursement.setAmount(resultSet.getInt("amount"));
                reimbursement.setSubmitted(resultSet.getTimestamp("submitted"));
                reimbursement.setResolved(resultSet.getTimestamp("resolved"));
                reimbursement.setDescription(resultSet.getString("description"));
                //reimbursement.setReceipt((Oid) resultSet.getBlob("receipt"));
                reimbursement.setPayment_id(resultSet.getString("payment_id"));
                reimbursement.setAuthor_id(resultSet.getString("author_id"));
                reimbursement.setResolver_id(resultSet.getString("resolver_id"));
                reimbursement.setStatus_id(resultSet.getString("status_id"));
                reimbursement.setType_id(resultSet.getString("type_id"));
                reimbursements.add(reimbursement);
            }

        return reimbursements;

    }

    public void log(String level, String message) throws RuntimeException {
        try {
            File logFile = new File("logs/aap.log");
            logFile.createNewFile();
            BufferedWriter logWriter = new BufferedWriter(new FileWriter(logFile, true));
            logWriter.write(String.format("[%s] at %s logged: [%s] %s\n", Thread.currentThread().getName(), LocalDate.now(),
                    level.toUpperCase(), message));
            logWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}



