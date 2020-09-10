package com.raju.graphics.controller;

import com.raju.graphics.models.Block;
import com.raju.graphics.service.ShapeService;
import com.raju.messanger.Sender;
import javafx.event.EventHandler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

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

    public EventHandler getClickEventHandler(Block selectedBlock) {
        EventHandler eventHandler = event -> {
            ShapeService.getInstance().setSelectedBlock(selectedBlock);
            System.out.println("handle click event Raj "+ShapeService.getInstance().getSelectedBlock().getNode().getId());
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
