package com.example.server;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

public class Server {
    public void runServer(String[] args) {
        int status = 0;
        Communicator communicator = null;

        try {
            communicator = Util.initialize(args);

            ObjectAdapter caseOffice = communicator.createObjectAdapter("CaseOffice");
            OfficeI officeI = new OfficeI();

            caseOffice.add(officeI, Util.stringToIdentity("Office"));

            caseOffice.activate();

            System.out.println("CaseOffice started...");

            communicator.waitForShutdown();

        } catch (Exception e) {
            e.printStackTrace();
            status = 1;
        }
        if (communicator != null) {
            try {
                communicator.destroy();
            } catch (Exception e) {
                e.printStackTrace();
                status = 1;
            }
        }
        System.exit(status);
    }


    public static void main(String[] args) {
        Server app = new Server();
        app.runServer(args);
    }
}
