package com.raju.graphics.models;

import com.raju.graphics.enums.PieceType;
import javafx.scene.Group;
import javafx.scene.Node;

public class Block {
    private Group node;
    private boolean isFree = true;
    private int blockNum;
    private Boolean isWhite = null;      // to maintain that the block is occupied by which opponent for white player it is true else for black it is false
    private PieceType pieceType;

    public Block(Group node, int blockNum, boolean isWhite) {
        this.node = node;
        this.blockNum = blockNum;
        this.isWhite = isWhite;
    }

    public Group getNode() {
        return node;
    }

    public void setNode(Group node) {
        this.node = node;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public int getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(int blockNum) {
        this.blockNum = blockNum;
    }

    public Boolean getWhite() {
        return isWhite;
    }

    public void setWhite(Boolean white) {
        isWhite = white;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }

    @Override
    public String toString() {
        return "Block{" +
                "isFree=" + isFree +
                ", blockNum=" + blockNum +
                ", isWhite=" + isWhite +
                ", pieceType=" + pieceType +
                '}';
    }
}
