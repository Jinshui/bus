package com.bus.services.model;

import java.util.ArrayList;
import java.util.List;

public class ReservationMgmtDetail {
    private String routeId;
    private String routeName;
    private Vehicle vehicle;
    private Integer date;
    private Integer time;
    private Integer reservedSeatsCount;
    private Integer availableSeatsCount;
    private List<Passenger> passengers;

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getReservedSeatsCount() {
        return reservedSeatsCount;
    }

    public void setReservedSeatsCount(Integer reservedSeatsCount) {
        this.reservedSeatsCount = reservedSeatsCount;
    }

    public Integer getAvailableSeatsCount() {
        return availableSeatsCount;
    }

    public void setAvailableSeatsCount(Integer availableSeatsCount) {
        this.availableSeatsCount = availableSeatsCount;
    }

    public List<Passenger> getPassengers() {
        if(passengers == null){
            passengers = new ArrayList<>();
        }
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }
}
