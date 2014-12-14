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

public class MapFileServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MapFileServlet.class);
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String absolutionPath = "";
        try{
            WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
            RoutesService routesService = (RoutesService)wac.getBean("routesService");
            String filePath = req.getRequestURI().replaceFirst(req.getContextPath(), "");
            absolutionPath = routesService.getUploadPath() + filePath;
            byte[] imageBytes = IOUtils.readBytesFromStream(new FileInputStream(new File(absolutionPath)));
            resp.getOutputStream().write(imageBytes);
            resp.getOutputStream().flush();
        }catch (Exception e){
            log.error("Error handling map request " + absolutionPath + " due to: ", e);
        }
    }
}
