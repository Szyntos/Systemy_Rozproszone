package org.example.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    int ID;
    public static void main(String[] args) {
        new Client().runClient(); // Create an instance and call a non-static method
    }

    public void runClient() {
        System.out.println("Enter your messages (type 'quit' to exit): ");
        String hostName = "localhost";
        int portNumber = 12345;

        try (Socket socket = new Socket(hostName, portNumber);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            // Thread for reading server's messages

            ServerReader serverReader = new ServerReader(socket, this);

            serverReader.start();



            // Send a nickname or initial message if required
            out.println("Nick=Appa");


            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                serverReader.exit();
                out.println("_____userQuit_____");

            }));

            // Send messages to the server in the main thread

            String userInput;
            while (true) {
                userInput = scanner.nextLine();

                if ("quit".equalsIgnoreCase(userInput)) {
                    serverReader.exit();
                    out.println("_____userQuit_____");
                    break; // Exit loop to quit
                }

                out.println(">" + userInput); // Send user input to server
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Socket auto-closed by try-with-resources
    }

    public void setID(int ID) {
        this.ID = ID;
        System.out.println("Your ID is set to " + ID);
    }
}
