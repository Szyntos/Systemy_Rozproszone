package org.example.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.example.utils.Utils;

public class ServerReader extends Thread{
    Socket socket;
    BufferedReader in;
    private volatile boolean exit = false;
    Client client;

    public ServerReader(Socket socket, Client client) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.client = client;
    }

    public void exit(){
        this.exit = true;
    }

    public void run() {
        try {
            String inputLine;
            inputLine = in.readLine();

            client.setID(Integer.parseInt(Utils.extractWithoutPrefix("ID=", inputLine)));
            System.out.print(">");
            while (!exit && (inputLine = in.readLine()) != null) {
                System.out.println(Utils.extractWithoutPrefix(">", inputLine));
                System.out.print(">");

            }
        } catch (IOException e) {
            System.out.println("Server connection closed.");
        }

    }
}
