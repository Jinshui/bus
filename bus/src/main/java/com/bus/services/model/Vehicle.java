package com.bus.services.model;

import com.bus.services.model.base.BaseMongoPersistent;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(collection = Vehicle.MONGO_COLLECTION)
public class Vehicle extends BaseMongoPersistent<Vehicle> {
    public final static String MONGO_COLLECTION = "Vehicles";
    private String name;
    private Integer seatCount;
    private String licenseTag;
    private String driverName;
    private String driverContact;
    private String company;
    private List<Integer> startPoints; // HHmm: 07:40 --> 740

    //The following fields are used for reservation management only
    @Transient
    private List<Integer> availableSeats;
    @Transient//date --> reservation list
    private Map<Integer, List<ReservationMgmtList.ReservationStat>> reservationStats;

    public List<Integer> getStartPoints() {
        if(startPoints == null){
            startPoints = new ArrayList<>();
        }
        return startPoints;
    }

    public void setStartPoints(List<Integer> startPoints) {
        this.startPoints = startPoints;
    }

    public Integer getSeatCount(){
        return seatCount;
    }

    public void setSeatCount(Integer seatCount){
        this.seatCount = seatCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany(){
        return company;
    }

    public void setCompany(String company){
        this. company = company;
    }

    public String getLicenseTag() {
        return licenseTag;
    }

    public void setLicenseTag(String licenseTag) {
        this.licenseTag = licenseTag;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverContact() {
        return driverContact;
    }

    public void setDriverContact(String driverContact) {
        this.driverContact = driverContact;
    }

    public Map<Integer, List<ReservationMgmtList.ReservationStat>> getReservationStats(){
        if(reservationStats == null){
            reservationStats = new HashMap<>();
        }
        return reservationStats;
    }

    public void setReservationStats(Map<Integer, List<ReservationMgmtList.ReservationStat>> reservationStats) {
        this.reservationStats = reservationStats;
    }

    public List<Integer> getAvailableSeats(){
        if(availableSeats == null){
            availableSeats = new ArrayList<>();
        }
        return availableSeats;
    }

    public void setAvailableSeats(List<Integer> availableSeats){
        this.availableSeats = availableSeats;
    }
}