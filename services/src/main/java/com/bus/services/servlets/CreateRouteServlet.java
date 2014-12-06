package com.bus.services.servlets;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.plexus.util.IOUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CreateRouteServlet extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filePath = "/home/jinshui/tmp/bus/uploads/" + System.currentTimeMillis();
        FileOutputStream fileOutputStream = null;
        InputStream is = null;
        try{
            fileOutputStream = new FileOutputStream(new File(filePath));
            is = req.getInputStream();
            byte[] data = new byte[1024];
            int length = is.read(data);
            while(length != -1){
                fileOutputStream.write(data);
                fileOutputStream.flush();
                length = is.read(data);
            }
        }finally {
            try{ is.close(); }catch (Exception e){}
            try{ fileOutputStream.close(); }catch (Exception e){}
        }
        resp.getWriter().write("Done");
    }
}
