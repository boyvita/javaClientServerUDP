package server;

import java.io.IOException;
import java.net.*;

public class UDPServer implements Runnable
{
    private final int serverPort;
    
    public UDPServer(int serverPort) {
        this.serverPort = serverPort;
    }
    
    @Override
    public void run() {
        try(DatagramSocket serverSocket = new DatagramSocket(serverPort)) {
            byte[] buffer = new byte[65507]; //64407 - max size
            while (true) {
                //показали как выглядит пакет
                DatagramPacket requestPacket = new DatagramPacket(
                        buffer,
                        0,
                        buffer.length
                );
                //получили пакет
                serverSocket.receive(requestPacket);
                
                
                //определили, что внутри
                int clientPort = requestPacket.getPort();
                String clientMessage = new String(requestPacket.getData());
                System.out.println(clientMessage + " recieved from port " + clientPort);
                
                //формируем ответ
                String serverResponseMessage = clientMessage.trim() + " + server Response!!!";
                System.out.println("server response message length is " + serverResponseMessage.length());
                DatagramPacket datagramResponsePacket = new DatagramPacket(
                        serverResponseMessage.getBytes(),
                        serverResponseMessage.length(),
                        InetAddress.getLocalHost(),
                        clientPort
                );
                serverSocket.send(datagramResponsePacket);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}