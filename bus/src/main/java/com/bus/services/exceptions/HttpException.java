
package com.bus.services.exceptions;

public class HttpException extends Exception {
    public HttpException(){
        super();
    }
    public HttpException(Exception e){
        super(e);
    }
    public HttpException(String msg){
        super(msg);
    }
    public HttpException(String msg, Exception e){
        super(msg, e);
    }
}
