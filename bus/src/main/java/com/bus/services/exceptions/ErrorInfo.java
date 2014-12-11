package com.bus.services.exceptions;

import java.io.Serializable;

public class ErrorInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    public ErrorInfo(){}

    public ErrorInfo(int errCode, int errSubCode, String message) {
        super();
        this.errCode = errCode;
        this.errSubCode = errSubCode;
        this.message = message;
    }

    private int errCode;
    private int errSubCode;
    private String message;

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public int getErrSubCode() {
        return errSubCode;
    }

    public void setErrSubCode(int errSubCode) {
        this.errSubCode = errSubCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}