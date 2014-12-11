package com.bus.services.config.cxf;

import com.bus.services.exceptions.ApplicationException;
import com.bus.services.exceptions.ErrorInfo;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.Locale;

public class InvokeFaultExceptionMapper implements ExceptionMapper {

    public Response toResponse(Throwable ex) {
        StackTraceElement[] trace = new StackTraceElement[1];
        trace[0] = ex.getStackTrace()[0];
        ex.setStackTrace(trace);
        Response.ResponseBuilder rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.type("application/json;charset=UTF-8");
        if (ex instanceof ApplicationException) {//自定义的异常类
            ApplicationException e = (ApplicationException) ex;
            rb.entity(e.getErrorInfo());
        }else{
            rb.entity(ex.getMessage());
        }
        rb.language(Locale.SIMPLIFIED_CHINESE);
        return rb.build();
    }

}