package com.bus.services.services;


import com.bus.services.model.Reservation;
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
    @PUT
    public Reservation addReservation(Reservation reservation){
        return reservationRepository.save(reservation);
    }
    @DELETE
    @Path("/{id}")
    public void deleteReservation(@PathParam("id")String id){
        reservationRepository.delete(id);
    }
}
