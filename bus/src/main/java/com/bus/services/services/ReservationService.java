package com.bus.services.services;


import com.bus.services.exceptions.ApplicationException;
import com.bus.services.model.*;
import com.bus.services.repositories.PassengerRepository;
import com.bus.services.repositories.ReservationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);
    @Resource
    ReservationRepository reservationRepository;
    @Resource
    PassengerRepository passengerRepository;
    @Autowired
    ObjectMapper objectMapper;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Reservation> find(@QueryParam("routeId")String routeId, @QueryParam("passengerId")String passengerId, @QueryParam("date")Integer date, @QueryParam("time")Integer time) {
        return reservationRepository.find(routeId, passengerId, date, time);
    }
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Reservation getReservation(@PathParam("id")String id){
        return reservationRepository.findOne(id);
    }
    @POST
    public Reservation addReservation(ReservationForm formData) throws ApplicationException {
        log.info("Create new reservation: {}", formData);
        Reservation reservation = reservationRepository.findOne(formData.getRouteId(), formData.getOpenId(), formData.getDate(), formData.getTime());
        if(reservation != null){
            if(reservation.getStatus() == Reservation.Status.DELETED){
                reservation.setStatus(Reservation.Status.NEW);
            }else{
                throw new ApplicationException(100, "duplicate reservation found!");
            }
        }else{
            reservation = new Reservation();
        }
        Passenger passenger = new Passenger();
        passenger.setId(formData.getOpenId());
        passenger.setPhone(formData.getPhone());
        passenger.setUserName(formData.getUserName());
        reservation.setPassenger(passenger);
        Vehicle vehicle = new Vehicle();
        vehicle.setId(formData.getVehicleId());
        Route route = new Route();
        route.setId(formData.getRouteId());
        reservation.setRoute(route);
        reservation.setVehicle(vehicle);
        reservation.setRouteId(formData.getRouteId());
        reservation.setDepartureTime(formData.getTime());
        reservation.setDate(formData.getDate());
        reservationRepository.save(reservation);
        return getReservation(reservation.getId());
    }
    @DELETE
    @Path("/{id}")
    public void deleteReservation(@PathParam("id")String id, @QueryParam("pid")String pid) throws ApplicationException {
        log.info("Delete reservation: {}", id);
        Reservation reservation = reservationRepository.findOne(id);
        if(reservation == null){
            throw new ApplicationException(404, "No such reservation: " + id);
        }
        reservation.setStatus(Reservation.Status.DELETED);
        reservationRepository.save(reservation);
    }
}
