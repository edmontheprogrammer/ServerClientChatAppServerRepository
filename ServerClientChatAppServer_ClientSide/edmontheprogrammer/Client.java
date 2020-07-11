package com.edmontheprogrammer;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame{


    private JTextField userText;
    private JTextArea chatWindow;
    // this is the output from the client going to the server
    private ObjectOutputStream output;
    // this is the input coming from the server or another client
    private ObjectInputStream input;
    private String message = "";
    // the IP address of the server
    private String serverIP;
    private Socket connection;

    // constructor to initialize the instance variables and GUI
    // This client needs an IP address to connect to, the IP address of the server, in our case we'll connect and use
    // the localhost, 127.0.0.1's IP address.
    public Client(String host) {
        super("Client");
        serverIP = host;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        sendMessage(event.getActionCommand());
                        userText.setText("");
                    }
                }
        );
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(300, 150);
        setVisible(true);
    }

    // connect to server: the sendMessage is the method that sends data from the client to the server or another client
    public void startRunning() {
        try {
            connectToServer();
            setupStreams();
            whileChatting();
        } catch (EOFException eofException) {
            showMessage("\n Client terminated connection");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            closeConnections();
        }
    }

    // this is the method that connects the client to the server
    // the client is responsible for connecting to the server not the other way around
    // (typically the server does not attempt to connect to the client; the server can connect to the
    // client but that does not usually how chat application works happen)
    private void connectToServer() throws IOException{
        showMessage("Attempting connection... \n");
        connection = new Socket(InetAddress.getByName(serverIP), 6789);
        showMessage("Connected to: " + connection.getInetAddress().getHostName());
    }

    // Set up streams to send and receive messages
    private void setupStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Streams setup and ready to go! \n");
    }

    // while chatting with the server or another client do the following
    private void whileChatting() throws IOException {
        ableToType(true);
        do {
            try {
                message = (String) input.readObject();
                showMessage("\n" + message);
            } catch (ClassNotFoundException classNotFoundException) {
                showMessage("\n Unknown object type");
            }
        } while (!message.equals("SERVER - END"));
    }

    //close the streams and sockets
    private void closeConnections() {
        showMessage("\n Close connections ... streams and sockets");
        ableToType(false);
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    // send messages to the server
    private void sendMessage(String message) {
        try {
            output.writeObject("CLIENT - " + message);
            output.flush();
            showMessage("\nCLIENT - " + message);
        } catch (IOException ioException) {
            chatWindow.append("\n ERROR: Something went wrong when sending message");
        }
    }

    // the show method updates the GUI so the messages will show up in the window
    private void showMessage(final String m) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        chatWindow.append(m);
                    }
                }
        );
    }

    // Give user permission to type messages into the textbox
    private void ableToType(final boolean tof) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        userText.setEditable(tof);
                    }
                }
        );
    }

}































