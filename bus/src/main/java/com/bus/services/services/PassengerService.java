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
public class PassengerService {
    @Resource
    PassengerRepository passengerRepository;
}
