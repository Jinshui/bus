package com.bus.services.exceptions;

public class ApplicationException extends Exception {
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
}
