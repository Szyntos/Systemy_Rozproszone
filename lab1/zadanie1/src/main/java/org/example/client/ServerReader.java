package org.example.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Arrays;

import org.example.utils.Utils;

public class ServerReader extends Thread{
    Socket socket;
    BufferedReader in;
    private volatile boolean exit = false;
    Client client;
    boolean UDP = false;
    DatagramSocket socketUDP;
    byte[] receiveBuffer;

    public ServerReader(Socket socket, Client client) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.client = client;
        this.UDP = false;



    }
    public ServerReader(DatagramSocket socket, Client client) throws IOException {
        this.socketUDP = socket;
        this.client = client;
        this.UDP = true;
        this.receiveBuffer = new byte[1024];


    }

    public void exit(){
        this.exit = true;
    }

    public void run() {
        try {

            if (!UDP){
                String inputLine;
                inputLine = in.readLine();

                client.setID(Integer.parseInt(Utils.extractWithoutPrefix("ID=", inputLine)));
                System.out.print(">");
                while (!exit && (inputLine = in.readLine()) != null) {
                    System.out.println(Utils.extractWithoutPrefix(">", inputLine));
                    System.out.print(">");

                }
            }else{
                System.out.println("INIT UDP");
                Arrays.fill(receiveBuffer, (byte)0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

                while (!exit) {
                    socketUDP.receive(receivePacket);
                    System.out.print("byUDP - ");
                    System.out.println(Utils.extractWithoutPrefix(">", new String(receivePacket.getData()).trim()));
                    System.out.print(">");

                }
            }

        } catch (IOException e) {
            System.out.println("Server connection closed.");
        }

    }
}
