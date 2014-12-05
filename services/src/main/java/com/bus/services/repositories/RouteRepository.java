package com.bus.services.repositories;

import com.bus.services.model.Route;
import com.bus.services.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public class RouteRepository  extends BaseRepository<Route> {
    public RouteRepository() {
        super(Route.class);
    }
}
