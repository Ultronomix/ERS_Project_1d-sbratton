package com.revature.ers.users;

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

public class UserDAO {

    /*private final String baseSelect = "SELECT id, given_name, surname, email, username, \"password\", salary " +
            "FROM workersapp.workers ";*/

    private final String baseSelect = "SELECT user_id, username, email, \"password\", given_name, surname, is_active, role_id " +
            "FROM ers.ers_users ";

    public List<User> getAllUsers() {

        List<User> allUsers = new ArrayList<>();

        try {
            //assert ConnectionFactory.getInstance() != null;
            try (Connection connection = ConnectionFactory.getInstance().getConnection()) {

                // JDBC Statement objects are subject to SQL Injections
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(baseSelect);

                allUsers = mapResultSet(resultSet);

            }
        } catch (SQLException e) {
            System.err.println("Something went wrong when communicating with the database!");
            e.printStackTrace();
        }

        return allUsers;

    }

    public Optional<User> findUserById(Integer id) {

        String sql = baseSelect + "WHERE user_id = ?";

        try {
            //assert ConnectionFactory.getInstance() != null;
            try (Connection connection = ConnectionFactory.getInstance().getConnection()) {

                // JDBC Statement objects are subject to SQL Injections
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setObject(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                return mapResultSet(resultSet).stream().findFirst();

            }
        } catch (SQLException e) {
            // TODO Log this exception
            throw new DataSourceException(e);
        }

    }
    // below will be used in replacement
    /* public Optional<User> findUserById(String user_id) {

        String sql = baseSelect + "WHERE user_id = ?";

        try {
            //assert ConnectionFactory.getInstance() != null;
            try (Connection connection = ConnectionFactory.getInstance().getConnection()) {

                // JDBC Statement objects are subject to SQL Injections
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setObject(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                return mapResultSet(resultSet).stream().findFirst();

            }
        } catch (SQLException e) {
            // TODO Log this exception
            throw new DataSourceException(e);
        }

    } */
    public Optional<User> findUserByUsername(String username) {

        String sql = baseSelect + "WHERE username = ?";

        try {
            //assert ConnectionFactory.getInstance() != null;
            try (Connection connection = ConnectionFactory.getInstance().getConnection()) {

                // JDBC Statement objects are subject to SQL Injections
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                return mapResultSet(resultSet).stream().findFirst();

            }
        } catch (SQLException e) {
            // TODO Log this exception
            throw new DataSourceException(e);
        }

    }

    public boolean isUsernameTaken(String username) {
        return findUserByUsername(username).isPresent();
    }

    public Optional<User> findUserByEmail(String email) {

        String sql = baseSelect + "WHERE email = ?";

        try {
            try (Connection connection = ConnectionFactory.getInstance().getConnection()) {

                // JDBC Statement objects are subject to SQL Injections
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();
                return mapResultSet(resultSet).stream().findFirst();

            }
        } catch (SQLException e) {
            // TODO Log this exception
            throw new DataSourceException(e);
        }

    }

    public boolean isEmailTaken(String email) {
        return findUserByEmail(email).isPresent();
    }

    public Optional<User> findUserByUsernameAndPassword(String username, String password) {

        String sql = baseSelect + "WHERE username = ? AND password = ?";

        try {
            //assert ConnectionFactory.getInstance() != null;
            try (Connection connection = ConnectionFactory.getInstance().getConnection()) {

                // JDBC Statement objects are subject to SQL Injections
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();
                return mapResultSet(resultSet).stream().findFirst();

            }
        } catch (SQLException e) {
            // TODO Log this exception
            throw new DataSourceException(e);
        }

    }

    public String save(User user) {

        /* String sql = "INSERT INTO workersapp.workers" +
                "(given_name, surname, email, username, password, salary)" +
                "VALUES (?, ?, ?, ?, ?, ?)"; */

        String sql = "INSERT INTO ers.ers_users" +
                "(username, email, password, given_name, surname, is_active, role_id)" +
                    "VALUES(?, ?, ?, ?, ?, ?, false, ?)";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {



            PreparedStatement preparedStatement = conn.prepareStatement(sql, new String[]{"user_id"});
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getGiven_name());
            preparedStatement.setString(5, user.getSurname());
            preparedStatement.setBoolean(6, user.getIs_active());
            preparedStatement.setString(7, user.getRole_id());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            user.setUser_id(resultSet.getString("user_id"));

        } catch (SQLException e) {
            log("Error", e.getMessage());
        }

        log("INFO", "Successfully persisted new user with user_id: " + user.getUser_id());

        return user.getUser_id();
    }

    private List<User> mapResultSet(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();
        while (resultSet.next()) {
                User user = new User();
                user.setUser_id(resultSet.getString("user_id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword("***********"); // done for security purpose
                user.setGiven_name(resultSet.getString("given_name"));
                user.setSurname(resultSet.getString("surname"));
                user.setIs_active(String.valueOf(resultSet.getBoolean("is_active")).isEmpty());
                user.setRole_id(resultSet.getString("role_id"));
                users.add(user);
        }

        return users;

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









