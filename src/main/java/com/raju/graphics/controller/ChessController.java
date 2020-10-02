package com.raju.graphics.controller;

import com.raju.constants.ProjectConstants;
import com.raju.graphics.enums.EventType;
import com.raju.graphics.enums.PieceType;
import com.raju.graphics.models.Block;
import com.raju.graphics.service.ChessMasterService;
import com.raju.graphics.service.ShapeService;
import com.raju.messanger.Sender;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ChessController {
    private static ChessController chessController;
    private ChessMasterService chessMasterService;

    private ChessController() {
        chessMasterService = ChessMasterService.getInstance();
    }

    public static ChessController getInstance() {
        if (chessController == null) {
            chessController = new ChessController();
        }
        return chessController;
    }

    public EventHandler getClickEventHandler(ShapeService shapeService, Block destinationBlock, boolean isFirstPlayer, boolean isMyTurn) {
        return event -> {
            System.out.println("line 37 click event happened "+destinationBlock.getBlockNum());
            Block selectedBlock = shapeService.getSelectedBlock();
            if (!isMyTurn || !isSelectOrMoveActionAllowed(selectedBlock,destinationBlock,isFirstPlayer)) {
                return;
            }
            String eventMsg = "";

            if (isPieceMoveEvent(selectedBlock, destinationBlock, isFirstPlayer)) {
                chessMasterService.movePiece(selectedBlock, destinationBlock);
                shapeService.setSelectedBlock(null);
                shapeService.setMyTurn(false);

                eventMsg = EventType.PIECE_MOVE.toString() + ProjectConstants.EVENT_SEPARATOR + selectedBlock.getBlockNum()
                        + ProjectConstants.EVENT_SEPARATOR + destinationBlock.getBlockNum() + ProjectConstants.EVENT_SEPARATOR +
                        chessMasterService.getKingBlockIndexForCheckMove(shapeService.getBlockMap(), destinationBlock, isFirstPlayer);
            } else {
                shapeService.setSelectedBlock(destinationBlock);
                eventMsg = EventType.PIECE_SELECT.toString() + ProjectConstants.EVENT_SEPARATOR + destinationBlock.getBlockNum();
                chessMasterService.handleClickEvent(shapeService, destinationBlock, isFirstPlayer);
            }
            pushMsgIntoSocket(eventMsg);
        };
    }

    private void pushMsgIntoSocket(String eventMsg) {
        PrintWriter pw = Sender.getPrintWriter();
        pw.println(eventMsg);
        pw.flush();
    }

    private boolean isPieceMoveEvent(Block srcBlock, Block destBlock, boolean isFirstPlayer) {
        return srcBlock != null && srcBlock.getWhite() == isFirstPlayer &&
                (destBlock.isFree() || destBlock.getWhite() != isFirstPlayer);
    }

    private boolean isSelectOrMoveActionAllowed(Block selectedBlock,Block destBlock,boolean isFirstPlayer){
        if (selectedBlock == null){
            return (!destBlock.isFree() && destBlock.getWhite() == isFirstPlayer);
        }
        return (selectedBlock.getBlockNum() != destBlock.getBlockNum());
    }

}
