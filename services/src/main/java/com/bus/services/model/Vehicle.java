package com.bus.services.model;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {
    private String name;
    private String licenseTag;
    private String driverName;
    private String driverContact;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

//        {
//        name: '巴士A1',
//        id  : '京CA0653',
//        driverName: '陈师傅',
//        driverPhone: '18888888888',
//        startPoints: ['07:20', '08:00', '08:40', 09:20]
//        }
