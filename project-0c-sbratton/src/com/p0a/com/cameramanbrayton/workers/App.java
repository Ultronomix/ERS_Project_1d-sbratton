package com.p0a.com.cameramanbrayton.workers;

import main.java.com.p0a.com.cameramanbrayton.workers.auth.AuthServlet;
import main.java.com.p0a.com.cameramanbrayton.workers.users.UserDAO;
import main.java.com.p0a.com.cameramanbrayton.workers.users.UserServlet;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class App {

    public static void main( String[] args ) throws LifecycleException {

        String docBase = System.getProperty("java.io.tmpdir");
        Tomcat webServer = new Tomcat();

        // Web Server base configurations
        webServer.setBaseDir(docBase);
        webServer.setPort(5000);
        webServer.getConnector(); // formality Required in order for the server to receive request

        // App component instantiation
        UserDAO userDAO = new UserDAO();
        UserServlet userServlet = new UserServlet(userDAO);
        AuthServlet authServlet = new AuthServlet(userDAO);

        // Web server context and servlet Configurations
        final String rootContext = "/workers";
        webServer.addContext(rootContext, docBase);
        webServer.addServlet(rootContext,"UserServlet", userServlet).addMapping("/users");
        webServer.addServlet(rootContext,"AuthServlet", authServlet).addMapping("/auth");

        // Starting and awaiting web request
        webServer.start();
        webServer.getServer().await();
        //System.out.println("Web application successfully started");

    }

}