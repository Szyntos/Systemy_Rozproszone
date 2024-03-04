package org.example.client;

import org.example.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class Client {

    int ID;
    String nick = "Appa";



    public static void main(String[] args) throws UnknownHostException {
        new Client().runClient();
    }

    public void runClient() throws UnknownHostException {
        System.out.println("Enter your messages (type 'quit' to exit): ");
        String hostName = "localhost";
        int portNumber = 12345;
        byte[] receiveBuffer = new byte[1024];
        DatagramPacket sendPacket;
        InetAddress address = InetAddress.getByName(hostName);


        try (Socket socket = new Socket(hostName, portNumber);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in);
             DatagramSocket socketUDP = new DatagramSocket()) {


            ServerReader serverReaderTCP = new ServerReader(socket, this);
            ServerReader serverReaderUDP = new ServerReader(socketUDP, this);

            serverReaderTCP.start();
            serverReaderUDP.start();

            out.println("Nick="+nick);
            out.println("UDPport="+socketUDP.getLocalPort());





            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                serverReaderTCP.exit();
                serverReaderUDP.exit();
                out.println("_____userQuit_____");

            }));


            String userInput;
            while (true) {
                userInput = scanner.nextLine();
                System.out.print(">");

                if ("quit".equalsIgnoreCase(userInput)) {
                    serverReaderTCP.exit();
                    out.println("_____userQuit_____");
                    break;
                }

                if (userInput.startsWith("U ")){
                    String msg;
                    if (userInput.equals("U cat")){
                        msg = (">ID=" + ID + "Nick=" + nick + "Content=>" + "U " + Utils.asciiArtCat);
                    } else if (userInput.equals("U PC")){
                        msg = (">ID=" + ID + "Nick=" + nick + "Content=>" + "U " + Utils.asciiArtPC);
                    }
                    else{
                        msg = (">ID=" + ID + "Nick=" + nick + "Content=>" + userInput);
                    }
                    sendPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, address, portNumber);
                    socketUDP.send(sendPacket);
                }else{
                    out.println(">" + userInput);
                }


            }

        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("Could not connect to the server");
        }
    }

    public void setID(int ID) {
        this.ID = ID;
        System.out.println("Your ID is set to " + ID);
    }
}
