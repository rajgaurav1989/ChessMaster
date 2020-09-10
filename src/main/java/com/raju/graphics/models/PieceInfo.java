package com.raju.graphics.models;

import com.raju.graphics.enums.MovementLimit;
import com.raju.graphics.enums.MovementType;

import java.util.List;

public class PieceInfo {
    private List<MovementType> movementTypeList;
    private MovementLimit movementLimit ;

    public PieceInfo(List<MovementType> movementTypeList, MovementLimit movementLimit) {
        this.movementTypeList = movementTypeList;
        this.movementLimit = movementLimit;
    }

    public List<MovementType> getMovementTypeList() {
        return movementTypeList;
    }

    public void setMovementTypeList(List<MovementType> movementTypeList) {
        this.movementTypeList = movementTypeList;
    }

    public MovementLimit getMovementLimit() {
        return movementLimit;
    }

    public void setMovementLimit(MovementLimit movementLimit) {
        this.movementLimit = movementLimit;
    }
}
