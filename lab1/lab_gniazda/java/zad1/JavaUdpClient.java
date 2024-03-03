package zad1;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class JavaUdpClient {

    public static void main(String args[]) throws Exception
    {
        System.out.println("JAVA UDP CLIENT");
        DatagramSocket socket = null;
        int portNumber = 9008;

        try {
            socket = new DatagramSocket();

            byte[] sendBuffer = "MSG from client".getBytes();
            byte[] receiveBuffer = new byte[1024];
            Arrays.fill(receiveBuffer, (byte)0);
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

            InetAddress address = InetAddress.getByName("localhost");
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, portNumber);
            socket.send(sendPacket);

            socket.receive(receivePacket);
            System.out.println("received msg: " + new String(receivePacket.getData()));

        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}
