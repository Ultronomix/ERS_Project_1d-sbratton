package com.p0a.com.cameramanbrayton.workers;

import main.java.com.p0a.com.cameramanbrayton.workers.common.util.AppContext;

public class App {

    public static void main( String[] args ) {

        new AppContext().startApp();

        /* try {
        Screen currentScreen = new WelcomeScreen();
            currentScreen.render();
        } catch (IOException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
        }

        UserDAO userDAO = new UserDAO();

        User newUser = new User();
        newUser.setGiven_name("Paul");
        newUser.setSurname("Newman");
        newUser.setEmail("pau234@revature.com");
        newUser.setUsername("pnewman");
        newUser.setPassword("p$$WORD");
        newUser.setSalary(3000000);

        int newUserId = userDAO.save(newUser);
        System.out.println(newUserId);

        System.out.println("+---------------------------");

        List<User> users = userDAO.getAllUsers();
        users.forEach(System.out::println);*/
    }

}