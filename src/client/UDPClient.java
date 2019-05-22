package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class UDPClient implements Runnable {
    private final int clientPort;
    private final int serverPort;
    
    UDPClient(int port, int serverPort) {
        this.clientPort = port;
        this.serverPort = serverPort;
    }
    
    @Override
    public void run() {
        ///здесь получаем сообщения с сервера
        //необходимо все запихнуть в отдельный поток
        //new Runnable() - анонимный класс реализующий интерфейс Runnable, дада, ранбл в ранбле
        try (DatagramSocket clientSocket = new DatagramSocket(clientPort)) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    //ответ будет такого размера
                    byte[] responseBuffer = new byte[65507]; //64407 - max size
                    while (true) {
                        //получаем пакет будет с такими хар-ми
                        DatagramPacket datagramResponsePacket = new DatagramPacket(responseBuffer, 0, responseBuffer.length);
                        //получаем и выводим
                        try {
                            clientSocket.receive(datagramResponsePacket);
                            String recievedMessage = new String(datagramResponsePacket.getData());
                            int port = datagramResponsePacket.getPort();
                            
                            System.out.println("response from " + port + " " + recievedMessage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    
                }
            });
            //чтобы получатель работал, надо запустить его
            thread.start();
            
            
            //здесь отправляем, консоль в наших руках
            while (true) {
                Scanner in = new Scanner(System.in);
                String requestMessage = in.nextLine();
                DatagramPacket datagramRequestPacket = new DatagramPacket(requestMessage.getBytes(), requestMessage.length(), InetAddress.getLocalHost(), serverPort);
                clientSocket.send(datagramRequestPacket);
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