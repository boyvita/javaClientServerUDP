package client;

import java.io.*;
import java.net.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.*;


public class ClientConsole {
    public static void main(String[] args) {
        int port = 50000 + (int)(Math.random() * 100);
        System.out.println("Клиент с портом " + port);
        UDPClient client = new UDPClient(port, 50000);
        Thread thread = new Thread(client, "Поток клиента с портом " + port);
        thread.start();
    }
}
