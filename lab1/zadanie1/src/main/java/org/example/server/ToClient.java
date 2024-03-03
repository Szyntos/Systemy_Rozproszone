package org.example.server;

import org.example.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class ToClient extends Thread {
    Socket clientSocket;
    int clientID;
    String nick = "";
    Server server;
    PrintWriter out;
    public ToClient(Socket clientSocket, int ID, Server server){
        this.clientID = ID;
        this.clientSocket = clientSocket;
        this.server = server;
    }

    public void run() {
        try {
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // read msg, send response
            String msg = in.readLine();
            nick = Utils.extractWithoutPrefix("Nick=", msg);
            out.println("ID=" + clientID);

            while (true){
                msg = in.readLine();
                if (Objects.equals(msg, "_____userQuit_____")){
                    System.out.println("User '" + nick + "' id = " + clientID + " Quit");
                    break;
                }
                System.out.println("msg = " + Utils.extractWithoutPrefix(">", msg));
                this.server.sendToAll(msg, clientID, nick);
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {

            this.server.deleteClient(this);
        }

    }

    public int getID(){
        return clientID;
    }

    public void sendMsgToClient(String msg){
        out.println(">" + msg);
    }

}
