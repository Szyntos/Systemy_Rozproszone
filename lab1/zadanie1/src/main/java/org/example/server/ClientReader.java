package org.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.example.utils.Utils;

public class ClientReader extends Thread{
    Socket socket;
    BufferedReader in;
    private volatile boolean exit = false;
    ToClient toClient;
    boolean UDP = false;
    DatagramSocket socketUDP;
    byte[] receiveBuffer;
    PrintWriter out;
    int UDPport = 0;
    Server server;
    Pattern pattern = Pattern.compile("ID=(\\d+)Nick=([^Content]+)Content=>U (.+)");
    Matcher matcher;

    public ClientReader(Socket socket, ToClient toClient) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.toClient = toClient;
        this.UDP = false;
        this.server = toClient.server;



    }
    public ClientReader(DatagramSocket socket, Server server) throws IOException {
        this.socketUDP = socket;
        this.UDP = true;
        this.receiveBuffer = new byte[1024];
        this.server = server;


    }

    public void exit(){
        this.exit = true;
    }

    public void run() {
        try {
            String msg;
            if (!UDP){

                out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // read msg, send response
                msg = in.readLine();
                toClient.nick = Utils.extractWithoutPrefix("Nick=", msg);
                out.println("ID=" + toClient.clientID);
                msg = in.readLine();
                toClient.UDPport = Integer.parseInt(Utils.extractWithoutPrefix("UDPport=", msg));

                while (!exit){
                    msg = in.readLine();
                    if (Objects.equals(msg, "_____userQuit_____")){
                        System.out.println("User '" + toClient.nick + "' id = " + toClient.clientID + " Quit");
                        toClient.exit();
                        break;

                    }
                    System.out.println("msg = " + Utils.extractWithoutPrefix(">", msg));
                    toClient.server.sendToAll(msg, toClient.clientID, toClient.nick, UDP, -1);
                }
            }else{
                Arrays.fill(receiveBuffer, (byte)0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

                while (!exit) {
                    Arrays.fill(receiveBuffer, (byte)0);
                    socketUDP.receive(receivePacket);
                    msg = new String(receivePacket.getData());
                    System.out.print("byUDP - ");
                    matcher = pattern.matcher(msg);

                    if (matcher.find()) {
                        String id = matcher.group(1);
                        String nick = matcher.group(2);
                        String content = Utils.extractWithoutPrefix(">ID="+id+"Nick="+nick+"Content=>U ", msg);
                        System.out.println("msg = " + id + nick + content);
                        server.sendToAll(("\n\n" + content), Integer.parseInt(id), nick, UDP, receivePacket.getPort());
                    } else {
                        System.out.println("No match found.");
                    }

                }
            }

        } catch (IOException e) {
            System.out.println("Server connection closed.");
        }

    }
}
