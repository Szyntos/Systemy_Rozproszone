package org.example.server;

import org.example.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Server {
    static List<ToClient> clientComms = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        new Server().runServer();
    }

    public void runServer() throws IOException {


        int portNumber = 12345;
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("TCP Server Started, Listening for Clients");

            while(true){


                Socket clientSocket = serverSocket.accept();
                int newClientID = getFreeID();
                ToClient newClient = new ToClient(clientSocket, newClientID, this);
                clientComms.add(newClient);
                newClient.start();
                System.out.println("New Client connected, ID: " +  newClientID);



            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if (serverSocket != null){
                serverSocket.close();
            }
        }
    }

    public static int getFreeID() {
        Set<Integer> usedIDs = new TreeSet<>();

        // Collect all IDs currently in use
        for (ToClient client : clientComms) {
            usedIDs.add(client.getID());
        }

        // Start from 1 and find the first free ID
        int currentID = 1;
        while (usedIDs.contains(currentID)) {
            currentID++;
        }

        return currentID;
    }

    public void deleteClient(ToClient toClient){
        clientComms.remove(toClient);
    }

    public void sendToAll(String msg, int fromID, String fromNick){
        for (ToClient toClient :
                clientComms) {
            if (toClient.clientID == fromID){
                continue;
            }
            toClient.sendMsgToClient(fromNick + " (" + fromID + ") : " + Utils.extractWithoutPrefix(">", msg));
        }
    }
}
