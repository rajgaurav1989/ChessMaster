package com.raju.messanger;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;

public class Sender extends Thread {
    private Socket outputSocket;
    private static BufferedReader br ;
    private static OutputStream outputStream ;
    private static PrintWriter pw;

    public Sender(Socket outputSocket) {
        this.outputSocket = outputSocket;
        try {
            outputStream = this.outputSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PrintWriter getPrintWriter(){
        if (pw == null){
            pw = new PrintWriter(new OutputStreamWriter(outputStream));
        }
        return pw;
    }

    @Override
    public void run() {
        try {
            while (true) {
            }
        }
        catch (Exception e){
            pw.close();
        }
    }

}
