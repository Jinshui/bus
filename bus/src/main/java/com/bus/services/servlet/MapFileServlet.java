package com.bus.services.servlet;

import com.bus.services.services.RoutesService;
import org.apache.cxf.helpers.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MapFileServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MapFileServlet.class);
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String absolutionPath = "";
        OutputStream os = null;
        try{
            WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
            RoutesService routesService = (RoutesService)wac.getBean("routesService");
            String filePath = req.getRequestURI().replaceFirst(req.getContextPath(), "");
            absolutionPath = routesService.getUploadPath() + filePath;
            os = resp.getOutputStream();
            IOUtils.copy(new FileInputStream(new File(absolutionPath)), os);
            os.flush();
        }catch (Exception e){
            log.error("Error handling map request " + absolutionPath + " due to: ", e);
        }finally{
            if(os != null){
                try{ os.close(); }catch (Exception e){}
            }
        }
    }
}
