package com.bus.services.model;

import com.bus.services.config.CascadeSave;
import com.bus.services.config.cxf.CustomDateSerializer;
import com.bus.services.model.base.BaseMongoPersistent;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = TimeRange.MONGO_COLLECTION)
public class TimeRange extends BaseMongoPersistent<TimeRange> {
    public final static String MONGO_COLLECTION = "TimeRanges";
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date startTime;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date endTime;
    private String startPoints;
    private boolean weekend;
    @DBRef
    @CascadeSave
    private List<Vehicle> vehicles;

    public List<Vehicle> getVehicles() {
        if(vehicles == null){
            vehicles = new ArrayList<>();
        }
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStartPoints() {
        return startPoints;
    }

    public void setStartPoints(String startPoints) {
        this.startPoints = startPoints;
    }

    public boolean isWeekend() {
        return weekend;
    }

    public void setWeekend(boolean weekend) {
        this.weekend = weekend;
    }
}