package org.acme.controlles;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import java.util.List;

import org.acme.entities.City;
import org.acme.validation.MessageResponse;

@Path("/city")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class city {

    @Inject
    Validator validator;

    @Inject
    org.acme.services.cityService cityService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<City> getAllCities() {
        return cityService.listAllCities();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public City getCity(@PathParam("id") Long id) {
        return cityService.getCity(id);
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCity(City city) {
        List<String> errorMessages = cityService.validateCity(city);
        if (errorMessages != null) {
            return Response.status(400)
                    .entity(new MessageResponse(false, String.join(", ", errorMessages)))
                    .build();
        }
        cityService.createCity(city);
        return Response.status(200)
                .entity(new MessageResponse(true, "City created successfully", city))
                .build();
    }

}
