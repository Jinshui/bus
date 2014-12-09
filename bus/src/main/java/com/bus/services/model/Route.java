package com.bus.services.model;

import com.bus.services.model.base.BaseMongoPersistent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = Route.MONGO_COLLECTION)
public class Route extends BaseMongoPersistent<Route> {
    public final static String MONGO_COLLECTION = "Routes";
    @Indexed
    private String name;
    private Integer price;
    private String startStation;
    private String startStationDesc;
    private String endStation;
    private String middleStations;
    private String mapFileName;
    private List<TimeRange> timeRanges;
    private boolean published;

    //Transient fields used by client
    @Transient
    private List<Reservation> reservations;
    @Transient
    private Date matchedDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStationDesc(String startStationDesc) {
        this.startStationDesc = startStationDesc;
    }

    public String getStartStationDesc() {
        return startStationDesc;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public String getMiddleStations() {
        return middleStations;
    }

    public void setMiddleStations(String middleStations) {
        this.middleStations = middleStations;
    }

    public String getMapFileName(){
        return mapFileName;
    }

    public void setMapFileName(String mapFileName){
        this.mapFileName = mapFileName;
    }

    public List<TimeRange> getTimeRanges() {
        if(timeRanges == null){
            timeRanges = new ArrayList<>();
        }
        return timeRanges;
    }

    public void setTimeRanges(List<TimeRange> timeRanges) {
        this.timeRanges = timeRanges;
    }

    public boolean isPublished(){
        return published;
    }

    public void setPublished(boolean published){
        this.published = published;
    }

    public List<Reservation> getReservations(){
        return reservations;
    }

    public void setReservations(List<Reservation> reservations){
        this.reservations = reservations;
    }

    public Date getMatchedDate() {
        return matchedDate;
    }

    public void setMatchedDate(Date matchedDate) {
        this.matchedDate = matchedDate;
    }
}
