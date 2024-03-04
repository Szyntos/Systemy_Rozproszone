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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
public class Server {
    static List<ToClient> clientComms = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        new Server().runServer();
    }

    public void runServer() throws IOException {


        int portNumber = 12345;
        ServerSocket serverSocket = null;
        DatagramSocket udpSocket = null;


        try {
            serverSocket = new ServerSocket(portNumber);
            udpSocket = new DatagramSocket(portNumber);
            ClientReader UDPReader = new ClientReader(udpSocket, this);
            UDPReader.start();

            System.out.println("Server Started, Listening for Clients");

            while(true){


                Socket clientSocket = serverSocket.accept();
                int newClientID = getFreeID();
                ToClient newClient = new ToClient(clientSocket, udpSocket, newClientID, this);
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

        for (ToClient client : clientComms) {
            usedIDs.add(client.getID());
        }

        int currentID = 1;
        while (usedIDs.contains(currentID)) {
            currentID++;
        }

        return currentID;
    }

    public void deleteClient(ToClient toClient){
        clientComms.remove(toClient);

    }

    public void sendToAll(String msg, int fromID, String fromNick, boolean UDP, int port) throws IOException {
        for (ToClient toClient :
                clientComms) {
            if ((!UDP && toClient.clientID == fromID) || (UDP && toClient.UDPport == port)){
                continue;
            }
            if (!UDP){
                toClient.sendMsgToClient(fromNick + " (" + fromID + ") : " + Utils.extractWithoutPrefix(">", msg), UDP);
            } else {
                toClient.sendMsgToClient(fromNick + " (" + fromID + ") : " + msg, UDP);

            }

        }
    }
}
