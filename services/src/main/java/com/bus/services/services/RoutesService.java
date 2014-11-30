package com.bus.services.services;

import com.bus.services.model.Route;
import org.springframework.stereotype.Service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;


@Service
@Path("/routes")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
/**
 * APIs for managing Routes
 * @since 1.0
 */
public class RoutesService {
    @GET
    public List<Route> getAllRoutes(){
        return new ArrayList<Route>();
    }
}
