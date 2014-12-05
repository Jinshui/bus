package com.bus.services.model;

import com.bus.services.model.base.BaseMongoPersistent;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class Route extends BaseMongoPersistent<Route> {
    @Indexed
    private String name;
    private Integer price;
    private String startStation;
    private String startStationDesc;
    private String endStation;
    private String middleStations;
    private String mapFileName;
    private List<TimeRange> timeRanges;

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
}
