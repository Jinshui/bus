package com.bus.services.model;

public class ReservationForm {
    private String routeId;
    private String vehicleId;
    private String userName;
    private String phone;
    private String openId;
    private int date;
    private int time;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String toString(){
        return "routeId: " + routeId +", vehicleId: "+vehicleId+", userName:"+userName+", phone: "+phone+", openId: "+openId+", date: "+date+", time: "+time;
    }
}
