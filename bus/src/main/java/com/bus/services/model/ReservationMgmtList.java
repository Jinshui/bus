package com.bus.services.model;

import java.util.ArrayList;
import java.util.List;

public class ReservationMgmtList {
    private String routeId;
    private String routeName;
    private List<Vehicle> vehicles;

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

    public List<Vehicle> getVehicles(){
        if(vehicles == null)
            vehicles = new ArrayList<>();
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles){
        this.vehicles = vehicles;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ReservationMgmtList))
            return false;
        ReservationMgmtList that = (ReservationMgmtList)o;
        return routeId.equals(that.routeId);
    }

    @Override
    public int hashCode() {
        return routeId != null ? routeId.hashCode() : 0;
    }

    public static class ReservationStat implements Comparable<ReservationStat>{
        private int time;
        private int seatCount;
        private int reservedSeatCount;
        private int avaliableSeatCount;

        public int getAvaliableSeatCount() {
            return avaliableSeatCount;
        }

        public void setAvaliableSeatCount(int avaliableSeatCount) {
            this.avaliableSeatCount = avaliableSeatCount;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getSeatCount() {
            return seatCount;
        }

        public void setSeatCount(int seatCount) {
            this.seatCount = seatCount;
        }

        public int getReservedSeatCount() {
            return reservedSeatCount;
        }

        public void setReservedSeatCount(int reservedSeatCount) {
            this.reservedSeatCount = reservedSeatCount;
        }

        @Override
        public int compareTo(ReservationStat o) {
            if(o == null)
                return -1;
            return time - o.getTime();
        }
    }
}
