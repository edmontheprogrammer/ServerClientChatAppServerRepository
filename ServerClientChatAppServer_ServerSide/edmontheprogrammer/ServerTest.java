package com.edmontheprogrammer;
import javax.swing.JFrame;

public class ServerTest {

    public static void main(String[] args) {
        Server serverObject = new Server();
        serverObject.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // running the server
        serverObject.startRunning();
    }
}
