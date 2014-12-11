package com.bus.services.services;


import com.bus.services.exceptions.ApplicationException;
import com.bus.services.model.Passenger;
import com.bus.services.model.Reservation;
import com.bus.services.model.ReservationForm;
import com.bus.services.repositories.PassengerRepository;
import com.bus.services.repositories.ReservationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Service
@Path("/reservations")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ReservationService {
    @Resource
    ReservationRepository reservationRepository;
    @Resource
    PassengerRepository passengerRepository;
    @Autowired
    ObjectMapper objectMapper;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Reservation> findByDate(String routeId, int date) {
        return reservationRepository.findByDate(routeId, date);
    }
    @GET
    @Path("/{id}")
    public Reservation getReservation(@PathParam("id")String id){
        return reservationRepository.findOne(id);
    }
    @POST
    public Reservation addReservation(ReservationForm formData) throws ApplicationException {
        Reservation reservation = reservationRepository.findByDateTime(formData.getRouteId(), formData.getDate(), formData.getTime());
        Passenger passenger = passengerRepository.findOne(formData.getOpenId());
        if(passenger == null){
            passenger = new Passenger();
            passenger.setId(formData.getOpenId());
        }
        passenger.setPhone(formData.getPhone());
        passenger.setUserName(formData.getUserName());
        if(reservation == null){
            reservation = new Reservation();
            reservation.setRouteId(formData.getRouteId());
            reservation.setDepartureTime(formData.getTime());
            reservation.setDate(formData.getDate());
        }
        if(reservation.getPassengers().contains(passenger)){
            throw new ApplicationException(100, "duplicate reservation found!");
        }
        passengerRepository.save(passenger);
        reservation.getPassengers().add(passenger);
        return reservationRepository.save(reservation);
    }
    @DELETE
    @Path("/{id}")
    public void deleteReservation(@PathParam("id")String id){
        reservationRepository.delete(id);
    }
}
