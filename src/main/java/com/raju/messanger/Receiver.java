package com.raju.messanger;

import com.raju.graphics.service.ChessMasterService;
import com.raju.graphics.service.MessengerService;
import com.raju.graphics.service.ShapeService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class Receiver extends Thread {
    private Socket inputSocket;
    private static Receiver receiver;
    private MessengerService messengerService;

    private Receiver(Socket inputSocket) {
        this.inputSocket = inputSocket;
        messengerService = MessengerService.getInstance();
    }

    public static Receiver getInstance(Socket inputSocket){
        if (receiver == null){
            receiver = new Receiver(inputSocket);
        }
        return receiver;
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(this.inputSocket.getInputStream()));
            while (true) {
                String msg = br.readLine();
                System.out.println("From Server : " + msg);
                if (msg == null) {
                    break;
                }
                messengerService.handleMessageFromSocket(msg);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
