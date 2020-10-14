package com.raju.graphics.service;

import com.raju.Exception.ChessException;
import com.raju.constants.ProjectConstants;
import com.raju.graphics.enums.ErrorCode;
import com.raju.graphics.enums.MovementLimit;
import com.raju.graphics.enums.MovementType;
import com.raju.graphics.enums.PieceType;
import com.raju.graphics.models.Block;
import com.raju.graphics.models.PieceInfo;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMasterService {
    private static ChessMasterService chessMasterService;

    private ChessMasterService() {

    }

    public static ChessMasterService getInstance() {
        if (chessMasterService == null) {
            chessMasterService = new ChessMasterService();
        }
        return chessMasterService;
    }

    public List<Block> getTargetBlocks(Map<Integer, Block> blockMap, Block srcBlock, boolean isFirstPlayer) throws ChessException {
        PieceType pieceType = srcBlock.getPieceType();
        PieceInfo pieceInfo = ProjectConstants.pieceInfoMap.get(pieceType);
        MovementLimit movementLimit = pieceInfo.getMovementLimit();
        List<Integer> targetIndices = pieceInfo.getMovementTypeList()
                .stream()
                .map(e -> getTargetBlocksForMovementType(blockMap, srcBlock, e, movementLimit, isFirstPlayer))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        if (!targetIndices.isEmpty()) {
            targetIndices.add(srcBlock.getBlockNum());
        }
        return targetIndices.stream().map(blockMap::get).collect(Collectors.toList());
    }

    private List<Integer> getTargetBlocksForMovementType(Map<Integer, Block> blockMap, Block srcBlock,
                                                         MovementType movementType, MovementLimit movementLimit, boolean isFirstPlayer) throws ChessException {
        switch (movementType) {
            case LINEAR:
                return getBlockIndicesForLinearMovement(blockMap, srcBlock, movementLimit, isFirstPlayer);
            case DIAGONAL:
                return getBlockIndicesForDiagonalMovement(blockMap, srcBlock, movementLimit, isFirstPlayer);
            case KNIGHTISH:
                return getBlockIndicesForKnightishMovement(blockMap, srcBlock, isFirstPlayer);
            case PAWNISH:
                return getBlockIndicesForPawnishMovement(blockMap, srcBlock, isFirstPlayer);
        }
        throw new ChessException(ErrorCode.INVALID_MOVE, ErrorCode.INVALID_MOVE.getErrCode());
    }

    private List<Integer> getBlockIndicesForLinearMovement(Map<Integer, Block> blockMap, Block srcBlock, MovementLimit movementLimit, boolean isFirstPlayer) {
        boolean isSingle = movementLimit == MovementLimit.SINGLE;

        List<Integer> indicesList = new ArrayList<>();
        int blockNum = srcBlock.getBlockNum();
        int rowIndex = getRowIndex(blockNum);
        int colIndex = getColIndex(blockNum);

        if (isSingle) {
            indicesList.addAll(getAllSingleLinearIndices(blockMap, rowIndex, colIndex, isFirstPlayer));
            return indicesList;
        }

        indicesList.addAll(getBlockIndicesInLinearDirection(blockMap, rowIndex, colIndex, -1, true, isFirstPlayer));
        indicesList.addAll(getBlockIndicesInLinearDirection(blockMap, rowIndex, colIndex, +1, true, isFirstPlayer));
        indicesList.addAll(getBlockIndicesInLinearDirection(blockMap, rowIndex, colIndex, -1, false, isFirstPlayer));
        indicesList.addAll(getBlockIndicesInLinearDirection(blockMap, rowIndex, colIndex, +1, false, isFirstPlayer));
        return indicesList;
    }

    private List<Integer> getBlockIndicesInLinearDirection(Map<Integer, Block> blockMap, int rowIndex,
                                                           int colIndex, int additionFactor, boolean isRowMovement, boolean isFirstPlayer) {
        List<Integer> indicesList = new ArrayList<>();
        int startIndex = (isRowMovement ? rowIndex : colIndex) + additionFactor;
        int limitIndex = isRowMovement ? ProjectConstants.NUM_ROWS - 1 : ProjectConstants.NUM_CELLS_IN_ROW - 1;
        while (startIndex >= 0 && startIndex <= limitIndex) {
            int tempIndex = isRowMovement ? getBlockNum(startIndex, colIndex) : getBlockNum(rowIndex, startIndex);
            Block block = blockMap.get(tempIndex);
            boolean isFreeBlock = block.isFree();
            if (isFreeBlock) {
                indicesList.add(tempIndex);
            } else if (block.getWhite() != null && block.getWhite() != isFirstPlayer) {
                indicesList.add(tempIndex);
                return indicesList;
            } else {
                return indicesList;
            }
            startIndex = startIndex + additionFactor;
        }
        return indicesList;
    }

    private int checkFreeOrIsOwnPieceBlock(Map<Integer, Block> blockMap, int rowIndex, int colIndex, boolean isFirstPlayer) {
        boolean isSafeBlock = isSafeRowColIndex(rowIndex, colIndex);
        if (!isSafeBlock) {
            return -1;
        }
        int blockNum = getBlockNum(rowIndex, colIndex);
        Block block = blockMap.get(blockNum);
        if (block.isFree() || (block.getWhite() != null && block.getWhite() != isFirstPlayer)) {
            return blockNum;
        }
        return -1;
    }

    private List<Integer> getAllSingleLinearIndices(Map<Integer, Block> blockMap, int rowIndex, int colIndex, boolean isFirstPlayer) {
        List<Integer> indicesList = new ArrayList<>();
        int index = checkFreeOrIsOwnPieceBlock(blockMap, rowIndex, colIndex - 1, isFirstPlayer);
        if (index >= 0) {
            indicesList.add(index);
        }
        index = checkFreeOrIsOwnPieceBlock(blockMap, rowIndex, colIndex + 1, isFirstPlayer);
        if (index >= 0) {
            indicesList.add(index);
        }
        index = checkFreeOrIsOwnPieceBlock(blockMap, rowIndex - 1, colIndex, isFirstPlayer);
        if (index >= 0) {
            indicesList.add(index);
        }
        index = checkFreeOrIsOwnPieceBlock(blockMap, rowIndex + 1, colIndex, isFirstPlayer);
        if (index >= 0) {
            indicesList.add(index);
        }
        return indicesList;
    }

    private int getBlockNum(int rowNum, int colNum) {
        return rowNum * ProjectConstants.NUM_CELLS_IN_ROW + colNum;
    }

    private boolean isSafeRowColIndex(int rowIndex, int colIndex) {
        return rowIndex >= 0 && rowIndex < ProjectConstants.NUM_ROWS && colIndex >= 0 && colIndex < ProjectConstants.NUM_CELLS_IN_ROW;
    }

    private List<Integer> getBlockIndicesForPawnishMovement(Map<Integer, Block> blockMap, Block srcBlock, boolean isFirstPlayer) {
        List<Integer> indicesList = new ArrayList<>();
        int blockNum = srcBlock.getBlockNum();
        int rowIndex = getRowIndex(blockNum);
        int colIndex = getColIndex(blockNum);

        int targetRowIndex = isFirstPlayer ? rowIndex + 1 : rowIndex - 1;
        int index = checkFreeOrIsOwnPieceBlock(blockMap, targetRowIndex, colIndex, isFirstPlayer);
        if (index >= 0) {
            indicesList.add(index);
        }

        index = checkFreeOrIsOwnPieceBlock(blockMap, targetRowIndex, colIndex - 1, isFirstPlayer);
        if (index >= 0) {
            indicesList.add(index);
        }

        index = checkFreeOrIsOwnPieceBlock(blockMap, targetRowIndex, colIndex + 1, isFirstPlayer);
        if (index >= 0) {
            indicesList.add(index);
        }
        return indicesList;
    }

    private int getRowIndex(int blockNum) {
        return blockNum / ProjectConstants.NUM_ROWS;
    }

    private int getColIndex(int blockNum) {
        return blockNum % ProjectConstants.NUM_CELLS_IN_ROW;
    }

    private List<Integer> getBlockIndicesForKnightishMovement(Map<Integer, Block> blockMap, Block srcBlock, boolean isFirstPlayer) {
        List<Integer> indicesList = new ArrayList<>();
        int blockNum = srcBlock.getBlockNum();
        int rowIndex = getRowIndex(blockNum);
        int colIndex = getColIndex(blockNum);
        int index = checkFreeOrIsOwnPieceBlock(blockMap, rowIndex - 1, colIndex - 2, isFirstPlayer);
        if (index >= 0) {
            indicesList.add(index);
        }
        index = checkFreeOrIsOwnPieceBlock(blockMap, rowIndex - 1, colIndex + 2, isFirstPlayer);
        if (index >= 0) {
            indicesList.add(index);
        }
        index = checkFreeOrIsOwnPieceBlock(blockMap, rowIndex + 1, colIndex - 2, isFirstPlayer);
        if (index >= 0) {
            indicesList.add(index);
        }
        index = checkFreeOrIsOwnPieceBlock(blockMap, rowIndex + 1, colIndex + 2, isFirstPlayer);
        if (index >= 0) {
            indicesList.add(index);
        }
        return indicesList;
    }

    private List<Integer> getBlockIndicesForDiagonalMovement(Map<Integer, Block> blockMap, Block srcBlock, MovementLimit movementLimit, boolean isFirstPlayer) {
        List<Integer> indicesList = new ArrayList<>();
        int blockNum = srcBlock.getBlockNum();
        int rowIndex = blockNum / ProjectConstants.NUM_ROWS;
        int colIndex = blockNum % ProjectConstants.NUM_CELLS_IN_ROW;
        if (movementLimit == MovementLimit.SINGLE) {
            indicesList.addAll(getAllSingleDiagonalIndices(blockMap, rowIndex, colIndex, isFirstPlayer));
            return indicesList;
        }

        indicesList.addAll(getAllDiagonalIndices(blockMap, rowIndex, colIndex, -1, -1, isFirstPlayer));
        indicesList.addAll(getAllDiagonalIndices(blockMap, rowIndex, colIndex, -1, +1, isFirstPlayer));
        indicesList.addAll(getAllDiagonalIndices(blockMap, rowIndex, colIndex, +1, -1, isFirstPlayer));
        indicesList.addAll(getAllDiagonalIndices(blockMap, rowIndex, colIndex, +1, +1, isFirstPlayer));
        return indicesList;
    }

    private List<Integer> getAllDiagonalIndices(Map<Integer, Block> blockMap, int rowIndex,
                                                int colIndex, int rowAddFactor, int colAddFactor, boolean isFirstPlayer) {
        List<Integer> indicesList = new ArrayList<>();

        int startRowIndex = rowIndex + rowAddFactor;
        int startColIndex = colIndex + colAddFactor;
        while (startRowIndex >= 0 && startRowIndex < ProjectConstants.NUM_ROWS && startColIndex >= 0 && startColIndex < ProjectConstants.NUM_CELLS_IN_ROW) {
            int tempIndex = getBlockNum(startRowIndex, startColIndex);
            Block block = blockMap.get(tempIndex);
            boolean isFreeBlock = block.isFree();
            if (isFreeBlock) {
                indicesList.add(tempIndex);
            } else if (block.getWhite() != null && block.getWhite() != isFirstPlayer) {
                indicesList.add(tempIndex);
                return indicesList;
            } else {
                return indicesList;
            }
            startRowIndex = startRowIndex + rowAddFactor;
            startColIndex = startColIndex + colAddFactor;
        }

        return indicesList;
    }

    private List<Integer> getAllSingleDiagonalIndices(Map<Integer, Block> blockMap, int rowIndex, int colIndex, boolean isFirstPlayer) {
        List<Integer> indicesList = new ArrayList<>();
        int index = checkFreeOrIsOwnPieceBlock(blockMap, rowIndex - 1, colIndex - 1, isFirstPlayer);
        if (index >= 0) {
            indicesList.add(index);
        }
        index = checkFreeOrIsOwnPieceBlock(blockMap, rowIndex - 1, colIndex + 1, isFirstPlayer);
        if (index >= 0) {
            indicesList.add(index);
        }
        index = checkFreeOrIsOwnPieceBlock(blockMap, rowIndex + 1, colIndex - 1, isFirstPlayer);
        if (index >= 0) {
            indicesList.add(index);
        }
        index = checkFreeOrIsOwnPieceBlock(blockMap, rowIndex + 1, colIndex + 1, isFirstPlayer);
        if (index >= 0) {
            indicesList.add(index);
        }
        return indicesList;
    }

    public void movePiece(Block srcBlock, Block destBlock) {
        List<Node> destChildNodes = destBlock.getNode().getChildren();
        List<Node> srcChildNodes = srcBlock.getNode().getChildren();
        if (!destBlock.isFree()) {
            destChildNodes.remove(0);
        }
        Node pieceNode = srcChildNodes.get(0);
        destChildNodes.add(0, pieceNode);
        srcChildNodes.remove(pieceNode);
        destBlock.setWhite(srcBlock.getWhite());
        srcBlock.setWhite(null);
        srcBlock.setFree(true);
        destBlock.setFree(false);
        destBlock.setPieceType(srcBlock.getPieceType());
        srcBlock.setPieceType(null);
        Rectangle rectangle = getRectangleFromBlock(srcBlock);
        rectangle.setFill(getFillColor(srcBlock.getBlockNum()));
        rectangle.setOpacity(1);
    }


    public void colorKingBlockForCheckMove(Block kingBlock, Paint paint) {
        Rectangle kingRectangle = getRectangleFromGroup(kingBlock.getNode());
        kingRectangle.setFill(paint);
    }

    public Rectangle getRectangleFromGroup(Group group) {
        int rectangleShapeIndex = group.getChildren().size() - 1;
        return (Rectangle) group.getChildren().get(rectangleShapeIndex);
    }

    public void handleClickEvent(ShapeService shapeService, Block selectedBlock, boolean isFirstPlayer) {
        Map<Integer, Block> blockMap = shapeService.getBlockMap();
        List<Block> targetBlocks = shapeService.getTargetBlockList();
        unColourPreviousTargetBlock(targetBlocks);
        targetBlocks = getTargetBlocks(blockMap, selectedBlock, isFirstPlayer);
        if (targetBlocks != null && !targetBlocks.isEmpty()) {
            fillColorInChessBlocks(targetBlocks);
        }
        shapeService.setTargetBlockList(targetBlocks);
    }

    public void fillColorInChessBlocks(List<Block> blockList) {
        blockList.forEach(element -> {
            Rectangle rectangle = getRectangleFromBlock(element);
            rectangle.setOpacity(ProjectConstants.OPACITY_FACTOR);
            rectangle.setFill(ProjectConstants.TARGET_COLOR);
        });
    }

    public void unColourPreviousTargetBlock(List<Block> blockList) {
        if (blockList == null || blockList.isEmpty()){
            return;
        }
        blockList.forEach(element -> {
            Rectangle rectangle = getRectangleFromBlock(element);
            Paint fillColor = getFillColor(element.getBlockNum());
            rectangle.setFill(fillColor);
            if (!element.isFree()) {
                rectangle.setOpacity(ProjectConstants.OPACITY_FACTOR);
            }
        });
        blockList.clear();
    }

    public Paint getFillColor(int blockNum) {
        int rowNum = blockNum / ProjectConstants.NUM_CELLS_IN_ROW;
        int colNum = blockNum % ProjectConstants.NUM_CELLS_IN_ROW;
        javafx.scene.paint.Paint startColor = rowNum % 2 == 0 ? javafx.scene.paint.Color.WHITE : javafx.scene.paint.Color.BLACK;
        javafx.scene.paint.Paint otherColor = startColor == javafx.scene.paint.Color.WHITE ? javafx.scene.paint.Color.BLACK : Color.WHITE;
        return colNum % 2 == 0 ? startColor : otherColor;
    }

    private Rectangle getRectangleFromBlock(Block element) {
        int lastRectangleIndex = element.getNode().getChildren().size() - 1;
        return (Rectangle) element.getNode().getChildren().get(lastRectangleIndex);
    }

    public int getKingBlockIndexForCheckMove(Map<Integer, Block> blockMap, Block block, boolean isFirstPlayer) {
        List<Block> targetBlocks = getTargetBlocks(blockMap, block, isFirstPlayer);
        Optional<Block> kingBlock =
                targetBlocks.stream().filter(element -> element.getPieceType() == PieceType.KING && element.getWhite() != isFirstPlayer).findFirst();
        if (!kingBlock.isPresent()) {
            return -1;
        }
        colorKingBlockForCheckMove(kingBlock.get(), ProjectConstants.CHECK_MOVE_COLOR);
        return kingBlock.get().getBlockNum();
    }

}
