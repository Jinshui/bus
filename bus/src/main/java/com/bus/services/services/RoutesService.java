package com.bus.services.services;

import com.bus.services.exceptions.ApplicationException;
import com.bus.services.model.Route;
import com.bus.services.repositories.RouteRepository;
import com.bus.services.util.CollectionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.helpers.FileUtils;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.List;


@Service
@Path("/routes")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class RoutesService {
    private static final Logger log = LoggerFactory.getLogger(RoutesService.class);
    @Value("${file.save.path}")
    private String uploadPath;
    @Resource
    RouteRepository routeRepository;
    @Autowired
    ObjectMapper objectMapper;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Route> getAllRoutes(){
        return routeRepository.findAll();
    }
    @GET
    @Path("/{id}")
    public Route getRoute(@PathParam("id")String id){
        return routeRepository.findOne(id);
    }
    @PUT
    public Route addRoute(Route route){
        return routeRepository.save(route);
    }
    @DELETE
    @Path("/{id}")
    public void deleteRoute(@PathParam("id")String id){
        routeRepository.delete(id);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Route addRoute(List<Attachment> attachments, @QueryParam("name")String name, @QueryParam("price")String price,
                          @QueryParam("startStation")String startStation, @QueryParam("startStationDesc")String startStationDesc,
                          @QueryParam("endStation")String endStation, @QueryParam("middleStations")String middleStations) throws ApplicationException{
        try{
            Route route = null;
            String mapFileName = null;
            for(Attachment attachment : attachments){
                String dataName = attachment.getDataHandler().getName();
                if(dataName.equals("data")){
                    String json = IOUtils.toString(attachment.getDataHandler().getInputStream(), "UTF-8");
                    log.debug("JSON body: {}", json);
                    route = objectMapper.readValue(json, Route.class);
                }else{ //file
                    mapFileName = "map_" + System.currentTimeMillis() + "_" + dataName;
                    String filePath = uploadPath + "/maps/" + mapFileName;
                    FileOutputStream fileOutputStream = null;
                    InputStream is = null;
                    try{
                        fileOutputStream = new FileOutputStream(new File(filePath));
                        is = attachment.getDataHandler().getInputStream();
                        byte[] data = new byte[1024];
                        int length = is.read(data);
                        while(length != -1){
                            fileOutputStream.write(data, 0, length);
                            fileOutputStream.flush();
                            length = is.read(data);
                        }
                    }finally {
                        try{ is.close(); }catch (Exception e){}
                        try{ fileOutputStream.close(); }catch (Exception e){}
                    }
                }
            }
            return routeRepository.save(route);
        }
        catch (Exception e) {
            log.warn("Failed to create route due to errors: ", e);
            throw new ApplicationException(e.getMessage());
        }
    }
}
