package com.bus.services.repositories;

import com.bus.services.model.Passenger;
import com.bus.services.model.Reservation;
import com.bus.services.repositories.base.BaseRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PassengerRepository extends BaseRepository<Passenger> {
    public PassengerRepository() {
        super(Passenger.class);
    }
}
