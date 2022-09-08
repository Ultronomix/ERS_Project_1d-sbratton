package com.revature.ers;

import com.revature.ers.auth.AuthService;
import com.revature.ers.auth.AuthServlet;
import com.revature.ers.users.UserDAO;
import com.revature.ers.users.UserService;
import com.revature.ers.users.UserServlet;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class ErsApp {

    public static void main( String[] args ) throws LifecycleException {

        String docBase = System.getProperty("java.io.tmpdir");
        Tomcat webServer = new Tomcat();

        // Web Server base configurations
        webServer.setBaseDir(docBase);
        webServer.setPort(5000);
        webServer.getConnector(); // formality Required in order for the server to receive request

        // ErsApp component instantiation
        UserDAO userDAO = new UserDAO();
        AuthService authService = new AuthService(userDAO);
        UserService userService = new UserService(userDAO);
        UserServlet userServlet = new UserServlet(userService);
        AuthServlet authServlet = new AuthServlet(authService);

        // Web server context and servlet Configurations
        final String rootContext = "/workers";
        webServer.addContext(rootContext, docBase);
        webServer.addServlet(rootContext,"UserServlet", userServlet).addMapping("/users");
        webServer.addServlet(rootContext,"AuthServlet", authServlet).addMapping("/auth");
        //webServer.addServlet(rootContext, "ReimbursementServlet", rembServlet).addMapping("/reimbursement");

        // Starting and awaiting web request
        webServer.start();
        webServer.getServer().await();
        //System.out.println("Web application successfully started");

    }

}