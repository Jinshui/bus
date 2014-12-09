package com.bus.services.model;

import java.util.List;

public class ReservationMgmt  {
    private String routeId;
    private String routeName;
    private List<Vehicle> vehicles;

    public static class ReservationStat{
        private int date;
        private List<Passenger> reservations;
    }

    public static class Reservations{
        private int time;
        private int seatCount;
        private List<Passenger> passengers;
    }
}
