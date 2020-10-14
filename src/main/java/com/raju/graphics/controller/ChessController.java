package com.raju.graphics.controller;

import com.raju.constants.ProjectConstants;
import com.raju.graphics.enums.EventType;
import com.raju.graphics.enums.PieceType;
import com.raju.graphics.models.Block;
import com.raju.graphics.service.ChessMasterService;
import com.raju.graphics.service.ShapeService;
import com.raju.messanger.Sender;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
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
            String opponentMsg = shapeService.getSocketMsg();
            boolean isMyPiece = isFirstPlayer;
            Block selectedBlock = shapeService.getSelectedBlock();
            if (isMyTurn && !isSelectOrMoveActionAllowed(selectedBlock,destinationBlock,isMyPiece) ||
                    (!isMyTurn && StringUtils.isBlank(opponentMsg))) {
                return;
            }
            if (!isMyTurn){
                isMyPiece = !isFirstPlayer;
            }
            String eventMsg = "";
            boolean isPieceMove ;
            if (StringUtils.isNotBlank(opponentMsg)){
                isPieceMove = StringUtils.equalsIgnoreCase("PIECE_MOVE",opponentMsg);
            }
            else {
                isPieceMove = isPieceMoveEvent(selectedBlock, destinationBlock, isMyPiece);
            }
            Map<Integer,Block> blockMap = shapeService.getBlockMap();
            if (isPieceMove) {
                boolean threadMyPiece = isMyPiece;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        chessMasterService.movePiece(selectedBlock, destinationBlock);
                        shapeService.setSelectedBlock(null);
                        shapeService.setMyTurn(!isMyTurn);
                        int kingBlockIndexForCheck = chessMasterService.getKingBlockIndexForCheckMove(blockMap, destinationBlock, threadMyPiece);

                        if (kingBlockIndexForCheck >= 0){
                            Block kingBlock = blockMap.get(kingBlockIndexForCheck) ;
                            chessMasterService.colorKingBlockForCheckMove(kingBlock, ProjectConstants.CHECK_MOVE_COLOR);
                            chessMasterService.colorKingBlockForCheckMove(destinationBlock,ProjectConstants.TARGET_COLOR);
                            List<Block> targetBlocks = new ArrayList<>();
                            targetBlocks.add(destinationBlock);
                            targetBlocks.add(kingBlock);
                            shapeService.setTargetBlockList(targetBlocks);
                        }

                        pushMsgIntoSocket(EventType.PIECE_MOVE.toString() + ProjectConstants.EVENT_SEPARATOR + selectedBlock.getBlockNum()
                                + ProjectConstants.EVENT_SEPARATOR + destinationBlock.getBlockNum() + ProjectConstants.EVENT_SEPARATOR);
                        shapeService.setSocketMsg(null);
                    }
                });
            } else {
                shapeService.setSelectedBlock(destinationBlock);
                eventMsg = EventType.PIECE_SELECT.toString() + ProjectConstants.EVENT_SEPARATOR + destinationBlock.getBlockNum();
                chessMasterService.handleClickEvent(shapeService, destinationBlock, isMyPiece);
                if(isMyTurn) {
                    pushMsgIntoSocket(eventMsg);
                }
                shapeService.setSocketMsg(null);
            }
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
