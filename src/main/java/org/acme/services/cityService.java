package org.acme.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.acme.entities.City;
import org.acme.repositories.cityRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class cityService {
    @Inject
    Validator validator;

    @Inject
    cityRepository cityRepository;

    public cityService() {

    }

    public List<String> validateCity(City city) {
        Set<ConstraintViolation<City>> violations = validator.validate(city);
        if (!violations.isEmpty()) {
            List<String> errorMessages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            return errorMessages;
        }
        return null;
    }

    public List<City> listAllCities() {
        return cityRepository.listAll();
    }

    public City getCity(Long id) {
        return cityRepository.findById(id);
    }

    public void createCity(City city) {
        cityRepository.persist(city);
    }

    /*
     * public Response createCity(City city) {
     * 
     * Set<ConstraintViolation<City>> violations = validator.validate(city);
     * if (!violations.isEmpty()) {
     * 
     * List<String> errorMessages = violations.stream()
     * .map(ConstraintViolation::getMessage)
     * .collect(Collectors.toList());
     * 
     * return Response.status(Status.BAD_REQUEST)
     * .entity(new MessageResponse(false, String.join(", ", errorMessages))) // Use
     * specific error messages
     * .build();
     * }
     * 
     * cityRepository.persist(city);
     * return Response.status(Status.CREATED)
     * .entity(new MessageResponse(true, "City created successfully", city))
     * .build();
     * }
     */

}
