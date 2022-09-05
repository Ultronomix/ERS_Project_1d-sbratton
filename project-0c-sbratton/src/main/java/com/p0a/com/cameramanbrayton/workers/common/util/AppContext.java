package main.java.com.p0a.com.cameramanbrayton.workers.common.util;

import main.java.com.p0a.com.cameramanbrayton.workers.users.User;
import main.java.com.p0a.com.cameramanbrayton.workers.users.UserDAO;

public class AppContext {

    private static boolean appRunning;

    public AppContext() {
        appRunning = true;
    }

    public void startApp() {
        while (appRunning) {
            try {
                UserDAO userDAO = new UserDAO();
                userDAO.getAllUsers().forEach(System.out::println);
                appRunning = false;
                /*System.out.println("The app is started, but will close immediately");
                appRunning = false;
                UserDAO userDAO = new UserDAO();
                //userDAO.getAllUsers().forEach(System.out::println);
                User loggedInUser = userDAO.findUserByUsernameAndPassword("pnewman", "p$$WORD")
                        .orElseThrow(() -> new RuntimeException("No User found with the provided credentials"));

                System.out.println("Successfully loggedIn user: " + loggedInUser);
                appRunning = false;*/
            } catch (Exception e) {
                e.printStackTrace();
                appRunning = false;
            }
        }
    }

    public static void shutdown() {
        appRunning = false;
    }

}
