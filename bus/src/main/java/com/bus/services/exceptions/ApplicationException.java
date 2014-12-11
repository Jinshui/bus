package com.bus.services.exceptions;

public class ApplicationException extends Exception {
    private int errCode;
    private int errSubCode;
    public ApplicationException(){
        super();
    }
    public ApplicationException(Exception e){
        super(e);
    }
    public ApplicationException(String msg){
        super(msg);
    }
    public ApplicationException(int code, String msg){
        super(msg);
        this.errCode = code;
    }
    public ApplicationException(int code, int subCode,  String msg){
        this(code, msg);
        this.errSubCode = subCode;
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

    public int getErrSubCode() {
        return errSubCode;
    }

    public void setErrSubCode(int errSubCode) {
        this.errSubCode = errSubCode;
    }

    public ErrorInfo getErrorInfo(){
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setErrCode(errCode);
        errorInfo.setErrSubCode(errSubCode);
        errorInfo.setMessage(getMessage());
        return errorInfo;
    }
}
