package com.raju;

import com.raju.constants.ProjectConstants;
import com.raju.graphics.service.ShapeService;
import com.raju.messanger.Receiver;
import com.raju.messanger.Sender;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.ServerSocket;
import java.net.Socket;


public class ChessMaster extends Application {

    public static void main(String[] args) {
        try {
            startSocket(args);
            launch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startSocket(String[] args){
        try {
            ShapeService shapeService = ShapeService.getInstance();
            String clientIP = args[0];
            int listenPort = Integer.parseInt(args[1]);
            int sendPort = Integer.parseInt(args[2]);
            boolean isfirst = Boolean.parseBoolean(args[3].toLowerCase());

            ServerSocket serverSocket;
            Socket sendSocket;
            Socket receiveSocket;
            if (isfirst) {
                shapeService.setWhite(true);
                serverSocket = new ServerSocket(listenPort);
                receiveSocket = serverSocket.accept();
                sendSocket = new Socket(clientIP, sendPort);
            } else {
                shapeService.setWhite(false);
                sendSocket = new Socket(clientIP, sendPort);
                serverSocket = new ServerSocket(listenPort);
                receiveSocket = serverSocket.accept();
            }
            Sender sender = new Sender(sendSocket);
            Receiver receiver = new Receiver(receiveSocket);

            sender.start();
            receiver.start();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Camera camera = new PerspectiveCamera();
        ShapeService shapeService = ShapeService.getInstance();
        Scene chessScene = new Scene(shapeService.getChessBoard(), ProjectConstants.WINDOW_SIZE, ProjectConstants.WINDOW_SIZE);
        chessScene.setCamera(camera);

        primaryStage.setTitle(ProjectConstants.TITLE);
        primaryStage.setResizable(false);
        primaryStage.setScene(chessScene);
        primaryStage.show();
    }
}
