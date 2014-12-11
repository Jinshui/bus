package com.bus.services.repositories;

import com.bus.services.model.Reservation;
import com.bus.services.model.Route;
import com.bus.services.repositories.base.BaseRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservationRepository extends BaseRepository<Reservation> {
    public ReservationRepository() {
        super(Reservation.class);
    }

    public List<Reservation> findBetweenDate(int date1, int date2) {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("date").gte(date1), Criteria.where("date").lte(date2));
        return findByCriteria(criteria);
    }

    public List<Reservation> findByDate(String routeId, int date) {
        return findByCriteria(Criteria.where("routeId").is(routeId).and("date").is(date));
    }

    public Reservation findByDateTime(String routeId, int date, int departureTime) {
        List<Reservation> list = findByCriteria(Criteria.where("routeId").is(routeId).and("date").is(date).and("departureTime").is(departureTime));
        if(list.isEmpty())
            return null;
        else
            return list.get(0);
    }
}