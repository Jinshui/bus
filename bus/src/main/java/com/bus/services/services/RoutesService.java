package com.bus.services.services;

import com.bus.services.exceptions.ApplicationException;
import com.bus.services.model.Route;
import com.bus.services.model.TimeRange;
import com.bus.services.model.Vehicle;
import com.bus.services.repositories.RouteRepository;
import com.bus.services.util.CollectionUtil;
import com.bus.services.util.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
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
import java.util.*;

import com.bus.services.model.Reservation;


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
    @Resource
    ReservationService reservationService;
    @Autowired
    ObjectMapper objectMapper;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Route> getAllRoutes(){
        return routeRepository.findAll();
    }

    private TimeRange findTimeRange(List<TimeRange> timeRanges, Calendar calendar, boolean compareTime){
        for(TimeRange timeRange : timeRanges){
            if(StringUtils.isNotEmpty(timeRange.getStartPoints())){
                List<String> timePointList = Arrays.asList(StringUtils.splitByWholeSeparator(timeRange.getStartPoints(), " "));
                Collections.sort(timePointList);
                String time = CollectionUtil.getLast(timePointList);
                if(time != null){
                    Calendar startCalendar = Calendar.getInstance();
                    startCalendar.setTime(timeRange.getStartTime());

                    Calendar endCalendar = Calendar.getInstance();
                    endCalendar.setTime(timeRange.getEndTime());
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    if( (dayOfWeek ==  Calendar.SUNDAY || dayOfWeek ==  Calendar.SATURDAY) && !timeRange.isWeekend() ){
                        continue;
                    }
                    if(compareTime && Integer.parseInt(time) <= DateUtil.getHHmm(calendar)) {
                        continue;
                    }
                    if(calendar.before(endCalendar) && calendar.after(startCalendar)){
                        return timeRange;
                    }
                }
            }
        }
        return null;
    }
    @GET
    @Path("/{id}")
    @Produces("application/json;charset=\"utf-8\"")
    public Route getRoute(@PathParam("id")String id){
        Route route = routeRepository.findOne(id);
        if(route == null)
            return null;

        TimeRange matchedTimeRange = null;
        Calendar calendar = Calendar.getInstance();
        if(CollectionUtil.isNotEmpty(route.getTimeRanges())){
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if(dayOfWeek <= Calendar.THURSDAY && dayOfWeek >= Calendar.MONDAY){ // Monday, Tuesday, Wednesday, Thursday
                matchedTimeRange = findTimeRange(route.getTimeRanges(), calendar, true);
                if(matchedTimeRange == null){ //Try nextDay
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    matchedTimeRange = findTimeRange(route.getTimeRanges(), calendar, false);
                }
            }else{ //Friday, Saturday, Sunday
                do{
                    matchedTimeRange = findTimeRange(route.getTimeRanges(), calendar, calendar.get(Calendar.DAY_OF_WEEK) == dayOfWeek);
                    if(matchedTimeRange == null){ //Try Saturday, Sunday, Monday
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                    }
                }while(matchedTimeRange == null && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.TUESDAY);
            }
        }

        route.getTimeRanges().clear();
        if(matchedTimeRange != null){
            route.getTimeRanges().add(matchedTimeRange);
            route.setClosestAvailableDate(calendar.getTime());
            List<Reservation> reservations = reservationService.find(route.getId(), null, DateUtil.getYyyyMMdd(calendar), null);

            if(reservations.size() > 0){
                for(Vehicle vehicle : matchedTimeRange.getVehicles()){
                    for(int i=0; i<vehicle.getStartPoints().size(); i++){
                        Integer startPoint = vehicle.getStartPoints().get(i);
                        vehicle.getAvailableSeats().add(i, vehicle.getSeatCount());
                        int reservedSeats = 0;
                        for(Reservation reservation : reservations){
                            if(reservation.getDepartureTime() == startPoint.intValue()){
                                reservedSeats ++;
                            }
                        }
                        vehicle.getAvailableSeats().set(i, vehicle.getSeatCount() - reservedSeats);
                    }
                }
            }else{
                for(Vehicle vehicle : matchedTimeRange.getVehicles()){
                    for(int i=0; i<vehicle.getStartPoints().size(); i++){
                        vehicle.getAvailableSeats().add(i, vehicle.getSeatCount());
                    }
                }
            }
        }

        return route;
    }

    @PUT
    public Route addRoute(Route route){
        if(route.getId() != null){
            Route existingRoute = routeRepository.findOne(route.getId());
            route.setMapFileName(existingRoute.getMapFileName());
        }
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
            if(mapFileName != null){
                route.setMapFileName("maps/" + mapFileName);
            }
            return routeRepository.save(route);
        }
        catch (Exception e) {
            log.warn("Failed to create route due to errors: ", e);
            throw new ApplicationException(e.getMessage());
        }
    }

    public String getUploadPath(){
        return uploadPath;
    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.get(Calendar.DAY_OF_WEEK));
    }
}
