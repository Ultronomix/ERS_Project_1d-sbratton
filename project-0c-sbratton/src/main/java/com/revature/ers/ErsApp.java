package com.revature.ers;

import com.revature.ers.auth.AuthService;
import com.revature.ers.auth.AuthServlet;
import com.revature.ers.reimbursement.RembDAO;
import com.revature.ers.reimbursement.RembService;
import com.revature.ers.reimbursement.RembServlet;
import com.revature.ers.users.UserDAO;
import com.revature.ers.users.UserService;
import com.revature.ers.users.UserServlet;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ErsApp {

    private static Logger logger = LogManager.getLogger(ErsApp.class);

    public static void main( String[] args ) throws LifecycleException {

        logger.info("Starting ErsApp");
        String docBase = System.getProperty("java.io.tmpdir");
        Tomcat webServer = new Tomcat();

        // Web Server base configurations
        webServer.setBaseDir(docBase);
        webServer.setPort(5000);
        webServer.getConnector(); // formality Required in order for the server to receive request

        // ErsApp component instantiation
        UserDAO userDAO = new UserDAO();
        RembDAO rembDAO = new RembDAO();
        AuthService authService = new AuthService(userDAO);
        UserService userService = new UserService(userDAO);
        UserServlet userServlet = new UserServlet(userService);
        AuthServlet authServlet = new AuthServlet(authService);
        RembService rembService = new RembService(rembDAO);
        RembServlet rembServlet = new RembServlet(rembService);


        // Web server context and servlet Configurations
        final String rootContext = "/ers";
        webServer.addContext(rootContext, docBase);
        webServer.addServlet(rootContext,"UserServlet", userServlet).addMapping("/users");
        webServer.addServlet(rootContext,"AuthServlet", authServlet).addMapping("/auth");
        webServer.addServlet(rootContext, "RembServlet", rembServlet).addMapping("/remb");

        // Starting and awaiting web request
        webServer.start();
        logger.info("ErsApp Web application successfully started");
        webServer.getServer().await();
        //System.out.println("Web application successfully started");



    }

}