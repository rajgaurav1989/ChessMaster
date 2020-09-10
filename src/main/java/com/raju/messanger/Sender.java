package com.raju.messanger;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Sender extends Thread {
    private Socket outputSocket;

    public Sender(Socket outputSocket) {
        this.outputSocket = outputSocket;
    }

    @Override
    public void run() {
        try {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(this.outputSocket.getOutputStream()));
            while (true) {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String msg = br.readLine();
                pw.println("Server Msg : " + msg);
                pw.flush();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
