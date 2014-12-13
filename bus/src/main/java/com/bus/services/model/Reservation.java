package com.bus.services.model;

import com.bus.services.config.CascadeSave;
import com.bus.services.model.base.BaseMongoPersistent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
@CompoundIndexes({
    @CompoundIndex(name="reservation_route_passenger_date_time", unique=true, def="{routeId:1, passenger.$id:1, date:1, departureTime:1}"),
    @CompoundIndex(name="reservation_route_passenger_date_time_status", def="{routeId:1, passenger.$id:1, date:1, departureTime:1, status:1}")
})
@Document(collection = Reservation.MONGO_COLLECTION)
public class Reservation extends BaseMongoPersistent<Reservation> {
    private static final Logger log = LoggerFactory.getLogger(Reservation.class);
    public final static String MONGO_COLLECTION = "Reservations";
    @Indexed
    private String routeId;
    @DBRef
    private Route route;
    @DBRef
    private Vehicle vehicle;
    @DBRef
    @CascadeSave
    private Passenger passenger;
    @Indexed
    private Integer date; //an integer representing for a date in yyyyMMdd format, e.g. 2014/12/13 -> 20141213
    private Integer departureTime; //an integer representing for a time in HHmm format, e.g. 10:00 -> 1000, 9:05 --> 905
    @Transient
    private Date fullDate;

    public static enum Status{
        DELETED, NEW, EXPIRED
    }
    private Status status = Status.NEW;

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public Route getRoute(){
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Vehicle getVehicle(){
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle){
        this.vehicle = vehicle;
    }

    public Passenger getPassenger(){
        return passenger;
    }

    public void setPassenger(Passenger passenger){
        this.passenger = passenger;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Integer getDepartureTime() {
        return departureTime;
    }

    public Date getFullDate(){
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HHmm");
            String strDate = "" + getDate() + " " + (getDepartureTime() > 1000 ? "" + getDepartureTime() : "0" + getDepartureTime()) ;
            return format.parse(strDate);
        }catch (Exception e){
            log.error("Failed to parse date time due to", e);
        }
        return null;
    }

    public void setStatus(Status status){
        this.status = status;
    }

    public Status getStatus(){
        return status;
    }

    public void setDepartureTime(Integer departureTime) {
        this.departureTime = departureTime;
    }
}
