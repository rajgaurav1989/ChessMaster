package com.raju.graphics.enums;

public enum ErrorCode {
    INVALID_MOVE("The move is invalid"),
    UNEXPECTED_ERROR("Some unexpected error has occurred");

    private String errCode ;

    public String getErrCode() {
        return errCode;
    }

    private ErrorCode(String errCode){
        this.errCode = errCode;
    }

}
