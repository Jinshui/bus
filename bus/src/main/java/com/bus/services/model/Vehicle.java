package com.bus.services.model;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {
    private String name;
    private int seatCount;
    private String licenseTag;
    private String driverName;
    private String driverContact;
    private String company;
    private List<String> startPoints;

    public List<String> getStartPoints() {
        if(startPoints == null){
            startPoints = new ArrayList<>();
        }
        return startPoints;
    }

    public void setStartPoints(List<String> startPoints) {
        this.startPoints = startPoints;
    }

    public int getSeatCount(){
        return seatCount;
    }

    public void setSeatCount(int seatCount){
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
}