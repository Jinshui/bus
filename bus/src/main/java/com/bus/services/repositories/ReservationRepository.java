package com.bus.services.repositories;

import com.bus.services.model.Reservation;
import com.bus.services.repositories.base.BaseRepository;
import com.bus.services.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class ReservationRepository extends BaseRepository<Reservation> {
    public ReservationRepository() {
        super(Reservation.class);
    }

    public List<Reservation> find(String routeId, String passengerId, Integer date, Integer time) {
        Criteria criteria = where("status").ne(Reservation.Status.DELETED.name());
        return findByCriteria(buildCriteria(criteria, routeId, passengerId, date, time));
    }

    public List<Reservation> findValidReservations(String passengerId) {
        int date = DateUtil.getYyyyMMdd(Calendar.getInstance());
        Criteria criteria = where("status").ne(Reservation.Status.DELETED.name()).and("date").gte(date);
        if(passengerId != null)
            criteria.and("passenger.$id").is(passengerId);
        List<Reservation> reservations = findByCriteria(criteria);
        if(reservations!=null){
            Iterator<Reservation> iterator = reservations.iterator();
            Calendar calendar = Calendar.getInstance();
            while(iterator.hasNext()){
                Reservation reservation = iterator.next();
                if(reservation.getDate() == DateUtil.getYyyyMMdd(calendar) && reservation.getDepartureTime() <= DateUtil.getHHmm(calendar)){
                    iterator.remove();
                }
            }
        }
        return reservations;
    }

    public Reservation findOne(String routeId, String passengerId, Integer date, Integer time) {
        List<Reservation> list = findByCriteria(buildCriteria(new Criteria(), routeId, passengerId, date, time));
        if(list.isEmpty())
            return null;
        else
            return list.get(0);
    }

    public List<Reservation> findBetween(Integer date1, Integer date2) {
        Criteria criteria = where("status").ne(Reservation.Status.DELETED.name());
        criteria.andOperator(where("date").gte(date1), where("date").lte(date2));
        return findByCriteria(criteria);
    }

    public List<Reservation> findAfter(Integer date) {
        Criteria criteria = where("status").ne(Reservation.Status.DELETED.name()).and("date").gte(date);
        return findByCriteria(criteria);
    }

    private Criteria buildCriteria(Criteria criteria, String routeId, String passengerId, Integer date, Integer time){
        if(StringUtils.isNotEmpty(routeId)){
            criteria.and("routeId").is(routeId);
        }
        if(StringUtils.isNotEmpty(passengerId)){
            criteria.and("passenger.$id").is(passengerId);
        }
        if(date != null){
            criteria.and("date").is(date);
        }
        if(time != null){
            criteria.and("departureTime").is(time);
        }
        return criteria;
    }
}