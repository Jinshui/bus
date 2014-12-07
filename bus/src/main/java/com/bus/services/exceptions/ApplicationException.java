package com.bus.services.exceptions;

public class ApplicationException extends Exception {
    private int errCode;
    private String errSubCode;
    public ApplicationException(){
        super();
    }
    public ApplicationException(Exception e){
        super(e);
    }
    public ApplicationException(String msg){
        super(msg);
    }
    public ApplicationException(String msg, Exception e){
        super(msg, e);
    }

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
}
