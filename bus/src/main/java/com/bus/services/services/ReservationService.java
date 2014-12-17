package com.bus.services.services;


import com.bus.services.exceptions.ApplicationException;
import com.bus.services.model.*;
import com.bus.services.repositories.PassengerRepository;
import com.bus.services.repositories.ReservationRepository;
import com.bus.services.util.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

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
        List<Reservation> reservations =  reservationRepository.find(routeId, passengerId, date, time);
        if(reservations!=null && !reservations.isEmpty()){
            for(Reservation reservation : reservations){
                reservation.getRoute().getTimeRanges().clear();
            }
        }
        return reservations;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Reservation find(@PathParam("id")String id){
        Reservation reservation = reservationRepository.findOne(id);
        if(reservation != null){
            reservation.getRoute().getTimeRanges().clear();
        }
        return reservation;
    }
    @GET
    @Path("/manage")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<ReservationMgmtList> getReservationStats(){
        Map<String, ReservationMgmtList> reservationMgmtMap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        int startDate = DateUtil.getYyyyMMdd(calendar);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        int endDate = DateUtil.getYyyyMMdd(calendar);
        List<Reservation> reservations = reservationRepository.findBetween(startDate, endDate);

        for(Reservation reservation : reservations){
            String routeId = reservation.getRoute().getId();
            ReservationMgmtList reservationMgmt = reservationMgmtMap.get(routeId);
            if(reservationMgmt == null){
                reservationMgmt = new ReservationMgmtList();
                reservationMgmt.setRouteId(routeId);
                reservationMgmt.setRouteName(reservation.getRoute().getName());
                reservationMgmtMap.put(routeId, reservationMgmt);
            }

            Vehicle vehicle = null;
            int existingIdx = reservationMgmt.getVehicles().indexOf(reservation.getVehicle());
            if(existingIdx >= 0){
                vehicle = reservationMgmt.getVehicles().get(existingIdx);
            }else{
                vehicle = new Vehicle();
                vehicle.setId(reservation.getVehicle().getId());
                vehicle.setName(reservation.getVehicle().getName());
                vehicle.setLicenseTag(reservation.getVehicle().getLicenseTag());
                vehicle.setDriverName(reservation.getVehicle().getDriverName());
                vehicle.setDriverContact(reservation.getVehicle().getDriverContact());
                vehicle.setSeatCount(reservation.getVehicle().getSeatCount());
                reservationMgmt.getVehicles().add(vehicle);
            }

            List<ReservationMgmtList.ReservationStat> reservationStats = vehicle.getReservationStats().get(reservation.getDate());
            if(reservationStats == null){
                reservationStats = new ArrayList<>();
                vehicle.getReservationStats().put(reservation.getDate(), reservationStats);
            }
            ReservationMgmtList.ReservationStat reservationStat = null;
            for(ReservationMgmtList.ReservationStat _reservationStat : reservationStats){
                if(_reservationStat.getTime() == reservation.getDepartureTime()){
                    reservationStat = _reservationStat;
                    break;
                }
            }
            if(reservationStat == null){
                reservationStat = new ReservationMgmtList.ReservationStat();
                reservationStat.setTime(reservation.getDepartureTime());
                reservationStat.setSeatCount(vehicle.getSeatCount());
                reservationStat.setAvaliableSeatCount(vehicle.getSeatCount());
                reservationStat.setReservedSeatCount(0);
                reservationStats.add(reservationStat);
                Collections.sort(reservationStats);
            }
            reservationStat.setAvaliableSeatCount(reservationStat.getAvaliableSeatCount() - 1);
            reservationStat.setReservedSeatCount(reservationStat.getReservedSeatCount() + 1);
        }
        return reservationMgmtMap.values();
    }
    @GET
    @Path("/manage/detail")
    @Produces(MediaType.APPLICATION_JSON)
    public ReservationMgmtDetail getReservationStatsDetail(@QueryParam("routeId")String routeId, @QueryParam("date")Integer date, @QueryParam("time")Integer time){
        List<Reservation> reservations = reservationRepository.find(routeId, null, date, time);
        ReservationMgmtDetail detail = new ReservationMgmtDetail();
        detail.setRouteId(routeId);
        detail.setDate(date);
        detail.setTime(time);
        for(int i=0; i<reservations.size(); i++){
            Reservation reservation = reservations.get(i);
            if(i == 0){
                detail.setRouteName(reservation.getRoute().getName());
                detail.setVehicle(reservation.getVehicle());
                detail.setAvailableSeatsCount(reservation.getVehicle().getSeatCount());
                detail.setReservedSeatsCount(0);
            }
            detail.getPassengers().add(reservation.getPassenger());
        }
        detail.setAvailableSeatsCount(detail.getAvailableSeatsCount() - reservations.size());
        detail.setReservedSeatsCount(reservations.size());
        return detail;
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
        return find(reservation.getId());
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
