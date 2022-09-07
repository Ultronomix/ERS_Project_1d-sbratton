package main.java.com.p0a.com.cameramanbrayton.workers.users;

import main.java.com.p0a.com.cameramanbrayton.workers.common.datasource.ConnectionFactory;
import main.java.com.p0a.com.cameramanbrayton.workers.common.datasource.exceptions.DataSourceException;

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

    private final String baseSelect = "SELECT id, given_name, surname, email, username, \"password\", salary " +
            "FROM workersapp.workers ";

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
            /*System.err.println("Something went wrong when communicating with the database!");
            e.printStackTrace();*/
        }

        //return Optional.empty();

    }

    public int save(User user) {

        String sql = "INSERT INTO workersapp.workers" +
                "(given_name, surname, email, username, password, salary)" +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {



            PreparedStatement preparedStatement = conn.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, user.getGiven_name());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getUsername());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setInt(6, user.getSalary());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            user.setId(resultSet.getInt("id"));

        } catch (SQLException e) {
            log("Error", e.getMessage());
        }

        log("INFO", "Successfully persisted new user with id: " + user.getId());

        return user.getId();
    }

    private List<User> mapResultSet(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();
        while (resultSet.next()) {
                User user = new User();
                user.setId(Integer.parseInt(String.valueOf(resultSet.getInt("id"))));
                user.setGiven_name(resultSet.getString("given_name"));
                user.setSurname(resultSet.getString("surname"));
                user.setEmail(resultSet.getString("email"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword("***********"); // done for security purpose
                user.setSalary(resultSet.getInt("salary"));
                users.add(user);
        }

        return users;

    }

        public void log(String level, String message) {
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









