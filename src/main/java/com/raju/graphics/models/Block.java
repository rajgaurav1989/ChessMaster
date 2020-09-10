package com.raju.graphics.models;

import com.raju.graphics.enums.PieceType;
import javafx.scene.Group;
import javafx.scene.Node;

public class Block {
    private Group node;
    private boolean isFree = true;
    private int blockNum;
    private boolean isWhite;
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

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean white) {
        isWhite = white;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }
}
