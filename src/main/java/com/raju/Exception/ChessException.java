package com.raju.Exception;


import com.raju.graphics.enums.ErrorCode;

public class ChessException extends RuntimeException {
    private String errorMsg;
    private ErrorCode errorCode;

    public ChessException(ErrorCode errorCode, String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }

}
