package org.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientReader extends Thread{
    Socket socket;
    BufferedReader in;
    private volatile boolean exit = false;

    public ClientReader(Socket socket) throws IOException {
        this.socket = socket;
        this.in =  new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void exit(){
        this.exit = true;
    }

    public void run() {
        try {
            String inputLine;
            while (!exit && (inputLine = in.readLine()) != null) {
                System.out.println("Server: " + inputLine);
            }
        } catch (IOException e) {
            System.out.println("Client connection closed.");
        }

    }
}
