package com.bus.services.model;

import com.bus.services.model.base.BaseMongoPersistent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@CompoundIndex(name="reservation_route_date_time", unique=true, def="{route:1, date:1, departureTime:1}")
@Document(collection = Reservation.MONGO_COLLECTION)
public class Reservation extends BaseMongoPersistent<Reservation> {
    public final static String MONGO_COLLECTION = "Reservations";
    private String routeId;
    @Indexed
    private Integer date; //an integer representing for a date in yyyyMMdd format, e.g. 2014/12/13 -> 20141213
    private Integer departureTime; //an integer representing for a time in HHmm format, e.g. 10:00 -> 1000, 9:05 --> 905
    @DBRef
    @JsonIgnore
    private List<Passenger> passengers;

    @Transient
    private int reservedSeats;
    @Transient
    private int availableSeats;

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Integer getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Integer departureTime) {
        this.departureTime = departureTime;
    }
}
