package org.example.server;

import org.example.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Objects;

public class ToClient extends Thread {
    Socket clientSocket;
    DatagramSocket socketUDP;
    int clientID;
    String nick = "";
    Server server;
    PrintWriter out;
    DatagramSocket udpSocket = new DatagramSocket();
    InetAddress address = InetAddress.getByName("localhost");
    int portNumber;
    ClientReader TCPReader;
    int UDPport = 0;
    public ToClient(Socket clientSocket, DatagramSocket socketUDP, int ID, Server server) throws IOException {
        this.clientID = ID;
        this.clientSocket = clientSocket;
        this.server = server;
        this.portNumber = clientSocket.getPort();
        this.socketUDP = socketUDP;
        this.TCPReader = new ClientReader(clientSocket, this);

    }


    public void run() {
        TCPReader.start();
    }

    public void exit(){
        TCPReader.exit();
        server.deleteClient(this);
    }

    public int getID(){
        return clientID;
    }

    public void sendMsgToClient(String msg, boolean UDP) throws IOException {
        if (UDP){
            DatagramPacket sendPacket = new DatagramPacket((">" + msg).getBytes(), (">" + msg).getBytes().length, address, UDPport);
            socketUDP.send(sendPacket);
            System.out.println("sentudp" + UDPport);
        } else {
            TCPReader.out.println(">" + msg);
        }


    }

}
