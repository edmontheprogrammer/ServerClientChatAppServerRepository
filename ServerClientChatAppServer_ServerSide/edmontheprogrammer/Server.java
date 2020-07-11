package com.edmontheprogrammer;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame{

    // creating instance variables for the chat

    // userText is the text entered by the user in the textbox
    private JTextField userText;
    // chatWindow is the TextArea that shows a history of all the chats
    private JTextArea chatWindow;
    // the outgoing connection to from one end-user to another end-user when the message is send
    private ObjectOutputStream output;
    // the incoming connection being recieved from one end-user to another end-user
    private ObjectInputStream input;
    // ServerSocket is basically a connection
    private ServerSocket server;
    // Socket means connection, it's the connection between your computer and another enduser
    private Socket connection;


    // constructor for the Server
    public Server() {
        super("Server Client Chat App");

        // initializing all the instance variables
        userText = new JTextField();
        userText.setEditable(false);
        // Whenever the user types text into the userText and presses enter, this method gets call, addActionListener()
        userText.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        sendMessage(event.getActionCommand());
                        userText.setText("");

                    }
                }
        );
        //adding the userText textbox to the JFrame
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        // adding the chatWindow textbox to the JFrame
        add(new JScrollPane(chatWindow));
        setSize(300, 150);
        setVisible(true);
    }

    // Set up and run the server. startRunning() is the method that sets-up the server and runs it.
    public void startRunning() {
        try {
            // creating a new connection with the port number 6789, ServerSocket is simply a connection
            server = new ServerSocket(6789, 100);
            while (true) {
                try {

                    waitForConnection();
                    setupStreams();
                    whileChatting();

                } catch (EOFException eofException) {
                    showMessage("\n Server Ended the Connection! ");
                } finally {
                    Closing_Connections();
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    // wait for connection, then display connection information such as the IP address
    private void waitForConnection() throws IOException {
        showMessage(" Waiting for someone to connect ... \n");
        connection = server.accept();
        showMessage(" Now connected to " + connection.getInetAddress().getHostName());
    }

    // Get stream to send and receive data
    // this method setups the output and input streams
    private void setupStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Streams are now setup \n");
    }

    // During the chat conversion running, do the following
    private void whileChatting() throws IOException {
        String message = " You are now connected! ";
        sendMessage(message);
        ableToType(true);
        do {
            try {
                message = (String) input.readObject();
                showMessage("\n" + message);

            }catch (ClassNotFoundException classNotFoundException) {
                showMessage("\n I don't know that user sent! \n");
            }
        } while (!message.equals("CLIENT - END "));
    }

    // Close streams and sockets after you are doing chatting
    // this method closes the input steam, output stream and the main connection between end users
    private void Closing_Connections() {
        showMessage("\n Closing connections \n");
        ableToType(false);
        try {
                output.close();
                input.close();
                connection.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    // send a message to the client
    private void sendMessage(String message) {
        try {
            output.writeObject("SERVER - " + message);
            output.flush();
            showMessage("\n SERVER - " + message);
        } catch (IOException ioException) {
            chatWindow.append("\n ERROR: MESSAGE CANNOT BE SEND");
        }
    }

    // updates chatWindow
    private void showMessage(final String text) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        chatWindow.append(text);
                    }
                }
        );
    }

    // let the user type stuff into their box
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
