package com.raju.graphics.controller;

import com.raju.graphics.models.Block;
import com.raju.graphics.service.ShapeService;
import com.raju.messanger.Sender;
import javafx.event.EventHandler;

import java.io.*;

public class ChessController {
    private static ChessController chessController ;

    private ChessController(){
    }

    public static ChessController getInstance(){
        if (chessController == null) {
            chessController = new ChessController();
        }
        return chessController;
    }

    public EventHandler getClickEventHandler(Block selectedBlock,boolean isWhite) {
        EventHandler eventHandler = event -> {
            if (selectedBlock.isFree() || selectedBlock.getWhite() == null || selectedBlock.getWhite() != isWhite){
                return;
            }
            ShapeService.getInstance().setSelectedBlock(selectedBlock);
            PrintWriter pw = Sender.getPrintWriter();
            pw.println(selectedBlock.getBlockNum());
            pw.flush();
        };
        return eventHandler;
    }

    public EventHandler getMoveEventHandler(Block selectedBlock, Block destBlock){
        EventHandler eventHandler = event -> {
            if (selectedBlock == null || selectedBlock.isFree()){
                return;
            }
            selectedBlock.setNode(null);
            selectedBlock.setFree(true);
            ShapeService.getInstance().setSelectedBlock(null);
        };
        return eventHandler;
    }

}
