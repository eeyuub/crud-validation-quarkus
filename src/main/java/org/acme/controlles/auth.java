package org.acme.controlles;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import org.acme.entities.User;
import org.acme.repositories.userRepository;
import org.acme.services.UserService;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import io.smallrye.jwt.build.Jwt;
import io.vertx.core.json.JsonObject;

@Path("auth")
public class auth {
    
    @Inject
    userRepository userRepository;

    @POST
    @Path("login")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(User user) {
        User foundUser = userRepository.findByEmail(user.getEmail());
        //return Response.ok(foundUser).build();
        if (foundUser != null && foundUser.getPassword().equals(user.getPassword())) {
            String token = Jwt.issuer("https://localhost/issuer")
                    .upn(user.getEmail())
                    .groups(new HashSet<>(Arrays.asList("User", "Admin")))
                    //.claim("name", user.getName())
                    .claim("email", user.getEmail())
                    .sign();
            return Response.ok(token).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}