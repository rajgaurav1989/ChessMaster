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
    private boolean isWhite;
    private Block selectedBlock = null ;

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
        int rowNum = blockNum / ProjectConstants.NUM_CELLS;
        int colNum = blockNum % ProjectConstants.NUM_CELLS;

        double xCoord = colNum * ProjectConstants.CELL_SIZE;
        double yCoord = rowNum * ProjectConstants.CELL_SIZE;

        Paint startColor = rowNum % 2 == 0 ? Color.WHITE : Color.BLACK;
        Paint otherColor = startColor == Color.WHITE ? Color.BLACK : Color.WHITE;
        Paint fillColor = colNum % 2 == 0 ? startColor : otherColor;

        Rectangle rectangle = new Rectangle(ProjectConstants.CELL_SIZE, ProjectConstants.CELL_SIZE, fillColor);

        Block block = new Block(group,blockNum,fillColor == Color.WHITE);
        if(ProjectConstants.initialPieceTypeMap.containsKey(blockNum)){
            addChessPieces(block,rectangle);
        }
        else {
            group.getChildren().add(rectangle);
        }
        group.translateXProperty().set(xCoord);
        group.translateYProperty().set(yCoord);
        group.setId(String.valueOf(blockNum));
        group.setOnMouseClicked(ChessController.getInstance().getClickEventHandler(block,isWhite));
        blockMap.put(blockNum,block);
        return group;
    }

    private void addChessPieces(Block block,Rectangle rectangle){
        Group group = block.getNode();
        int blockNum = block.getBlockNum();
        ImageView imageView = new ImageView(new Image(ProjectConstants.pathMap.get(blockNum)));
        imageView.setFitHeight(ProjectConstants.CELL_SIZE);
        imageView.setFitWidth(ProjectConstants.CELL_SIZE);
        block.setPieceType(ProjectConstants.initialPieceTypeMap.get(blockNum));
        block.setFree(false);
        block.setWhite(blockNum >= 0 && blockNum <= 15 ? true : false);
        group.getChildren().add(imageView);
        group.getChildren().add(rectangle);
        rectangle.setOpacity(ProjectConstants.OPACITY_FACTOR);
        blockMap.put(blockNum,block);
    }

    public Group getChessBoard() {
        int numCells = ProjectConstants.NUM_CELLS * ProjectConstants.NUM_CELLS;
        List<Node> chessBlocks = new ArrayList<>();
        for (int boardIndex = 0; boardIndex < numCells; boardIndex++) {
            chessBlocks.add(getBlock(boardIndex));
        }
        Group chessGroup = new Group();
        chessGroup.getChildren().addAll(chessBlocks);
        if (isWhite) {
            Rotate rotate = new Rotate(ProjectConstants.WHITE_BOARD_ROTATION_ANGLE,
                    ProjectConstants.WINDOW_SIZE/2 ,ProjectConstants.WINDOW_SIZE/2,0,Rotate.X_AXIS);
            chessGroup.getTransforms().add(rotate);
            adjustmentForWhitePlayer(0,15);
            adjustmentForWhitePlayer(48,63);
        }
        return chessGroup;
    }

    private void adjustmentForWhitePlayer(int startBlockNumber,int stopBlockNumber){
        for (int index = startBlockNumber ; index <= stopBlockNumber ; index++){
            Block block = blockMap.get(index);
            Rotate rotate = new Rotate(ProjectConstants.WHITE_BOARD_ROTATION_ANGLE,
                    ProjectConstants.CELL_SIZE/2 ,ProjectConstants.CELL_SIZE/2,0,Rotate.Z_AXIS);
            block.getNode().getTransforms().clear();
            block.getNode().getTransforms().add(rotate);
        }
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean white) {
        isWhite = white;
    }

    public Block getSelectedBlock() {
        return selectedBlock;
    }

    public void setSelectedBlock(Block selectedBlock) {
        this.selectedBlock = selectedBlock;
    }
}
