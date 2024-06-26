package org.acme.repositories;

import org.acme.entities.City;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class cityRepository implements PanacheRepository<City>{
    
}
