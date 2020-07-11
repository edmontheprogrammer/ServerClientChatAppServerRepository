package com.edmontheprogrammer;
import javax.swing.JFrame;

public class ClientTest {

    public static void main(String[] args) {
        //Creating an instance of the Client class, "Bob"
        Client Bob;
        Bob = new Client("127.0.0.1");
        Bob.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Bob.startRunning();
    }
}
