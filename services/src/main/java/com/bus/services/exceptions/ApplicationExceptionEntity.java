package com.bus.services.exceptions;

import java.io.Serializable;

public class ApplicationExceptionEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public ApplicationExceptionEntity(){}

    public ApplicationExceptionEntity(int errCode, String errSubCode, String message) {
        super();
        this.errCode = errCode;
        this.errSubCode = errSubCode;
        this.message = message;
    }

    /*错误码，默认为0*/
    private int errCode;

    /*错误子码，自定义该值*/
    private String errSubCode;

    private String message;

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrSubCode() {
        return errSubCode;
    }

    public void setErrSubCode(String errSubCode) {
        this.errSubCode = errSubCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}