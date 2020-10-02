package com.raju.graphics.service;

import com.raju.constants.ProjectConstants;
import com.raju.graphics.controller.ChessController;
import com.raju.graphics.models.Block;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShapeService {
    private static ShapeService shapeService;
    private Map<Integer, Block> blockMap = new HashMap<>();
    private boolean isfirstPlayer;
    private boolean isMyTurn;
    private Block selectedBlock;
    private Block destinationBlock;
    private List<Block> targetBlockList;

    private ShapeService() {

    }

    public static ShapeService getInstance() {
        if (shapeService == null) {
            shapeService = new ShapeService();
        }
        return shapeService;
    }

    private Node getBlock(int blockNum) {
        Group group = new Group();
        int rowNum = blockNum / ProjectConstants.NUM_CELLS_IN_ROW;
        int colNum = blockNum % ProjectConstants.NUM_CELLS_IN_ROW;

        double xCoord = colNum * ProjectConstants.CELL_SIZE;
        double yCoord = rowNum * ProjectConstants.CELL_SIZE;

        Paint startColor = rowNum % 2 == 0 ? Color.WHITE : Color.BLACK;
        Paint otherColor = startColor == Color.WHITE ? Color.BLACK : Color.WHITE;
        Paint fillColor = colNum % 2 == 0 ? startColor : otherColor;

        Rectangle rectangle = new Rectangle(ProjectConstants.CELL_SIZE, ProjectConstants.CELL_SIZE, fillColor);

        Block block = new Block(group, blockNum, fillColor == Color.WHITE);
        if (ProjectConstants.initialPieceTypeMap.containsKey(blockNum)) {
            addChessPieces(block, rectangle);
        } else {
            group.getChildren().add(rectangle);
        }
        group.translateXProperty().set(xCoord);
        group.translateYProperty().set(yCoord);
        group.setId(String.valueOf(blockNum));
        group.setOnMouseClicked(ChessController.getInstance().getClickEventHandler(shapeService, block, isfirstPlayer,isMyTurn));
        blockMap.put(blockNum, block);
        return group;
    }

    private void addChessPieces(Block block, Rectangle rectangle) {
        Group group = block.getNode();
        int blockNum = block.getBlockNum();
        ImageView imageView = new ImageView(new Image(ProjectConstants.pathMap.get(blockNum)));
        imageView.setFitHeight(ProjectConstants.CELL_SIZE);
        imageView.setFitWidth(ProjectConstants.CELL_SIZE);
        block.setPieceType(ProjectConstants.initialPieceTypeMap.get(blockNum));
        block.setFree(false);
        block.setWhite(blockNum >= 0 && blockNum <= 15);
        group.getChildren().add(imageView);
        group.getChildren().add(rectangle);
        rectangle.setOpacity(ProjectConstants.OPACITY_FACTOR);
        blockMap.put(blockNum, block);
    }

    public Group getChessBoard(boolean isFirstPlayer) {
        this.isfirstPlayer = isFirstPlayer;
        this.isMyTurn = isFirstPlayer;
        int numCells = ProjectConstants.NUM_CELLS_IN_ROW * ProjectConstants.NUM_CELLS_IN_ROW;
        List<Node> chessBlocks = new ArrayList<>();
        for (int boardIndex = 0; boardIndex < numCells; boardIndex++) {
            chessBlocks.add(getBlock(boardIndex));
        }
        Group chessGroup = new Group();
        chessGroup.getChildren().addAll(chessBlocks);
        if (isFirstPlayer) {
            Rotate rotate = new Rotate(ProjectConstants.WHITE_BOARD_ROTATION_ANGLE,
                    ProjectConstants.WINDOW_SIZE / 2, ProjectConstants.WINDOW_SIZE / 2, 0, Rotate.X_AXIS);
            chessGroup.getTransforms().add(rotate);
            adjustmentForWhitePlayer(0, 15);
            adjustmentForWhitePlayer(48, 63);
        }
        return chessGroup;
    }

    private void adjustmentForWhitePlayer(int startBlockNumber, int stopBlockNumber) {
        for (int index = startBlockNumber; index <= stopBlockNumber; index++) {
            Block block = blockMap.get(index);
            Rotate rotate = new Rotate(ProjectConstants.WHITE_BOARD_ROTATION_ANGLE,
                    ProjectConstants.CELL_SIZE / 2, ProjectConstants.CELL_SIZE / 2, 0, Rotate.Z_AXIS);
            block.getNode().getTransforms().clear();
            block.getNode().getTransforms().add(rotate);
        }
    }

    public Map<Integer, Block> getBlockMap() {
        return blockMap;
    }

    public boolean isIsfirstPlayer() {
        return isfirstPlayer;
    }

    public void setIsfirstPlayer(boolean isfirstPlayer) {
        this.isfirstPlayer = isfirstPlayer;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public void setMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }

    public Block getSelectedBlock() {
        return selectedBlock;
    }

    public void setSelectedBlock(Block selectedBlock) {
        this.selectedBlock = selectedBlock;
    }

    public Block getDestinationBlock() {
        return destinationBlock;
    }

    public void setDestinationBlock(Block destinationBlock) {
        this.destinationBlock = destinationBlock;
    }

    public List<Block> getTargetBlockList() {
        return targetBlockList;
    }

    public void setTargetBlockList(List<Block> targetBlockList) {
        this.targetBlockList = targetBlockList;
    }
}
