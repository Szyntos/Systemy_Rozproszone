package org.example.client;

import org.example.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MulticastReader extends Thread{
    boolean exit = false;
    Pattern pattern = Pattern.compile("ID=(\\d+)Nick=([^Content]+)Content=>M (.+)");
    Matcher matcher;
    public void exit(){
        exit = true;
    }
    public void run() {
        int portNumberMulti = 12346;
        String multicastGroup = "228.5.6.7";

        try (MulticastSocket multicastSocket = new MulticastSocket(portNumberMulti)) {
            InetAddress group = InetAddress.getByName(multicastGroup);
            multicastSocket.joinGroup(group);

            byte[] buffer = new byte[1024];
            while (!exit) {

//                System.out.println("INIT UDP MULTICAST");
                Arrays.fill(buffer, (byte)0);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                while (!exit) {
                    Arrays.fill(buffer, (byte)0);
                    multicastSocket.receive(packet);
                    String msg = new String(packet.getData()).trim();
                    matcher = pattern.matcher(msg);

                    if (matcher.find()) {
                        String id = matcher.group(1);
                        String nick = matcher.group(2);
                        String content = Utils.extractWithoutPrefix(">ID="+id+"Nick="+nick+"Content=>M ", msg);
                        System.out.print("byUDP - MULTICAST - ");
                        System.out.println( nick + " (" + id + ") : " + content);

                    } else {
                        System.out.println("No match found.");
                    }

                    System.out.print(">");

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server connection closed - Multicast.");
        }
    }
}