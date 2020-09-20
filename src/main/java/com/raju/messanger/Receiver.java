package com.raju.messanger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class Receiver extends Thread{
    private Socket inputSocket ;

    public Receiver(Socket inputSocket) {
        this.inputSocket = inputSocket;
    }

    @Override
    public void run(){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(this.inputSocket.getInputStream()));
            while(true){
                String msg = br.readLine() ;
                System.out.println("From Server : " +msg);
                if (msg == null){
                    break;
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
