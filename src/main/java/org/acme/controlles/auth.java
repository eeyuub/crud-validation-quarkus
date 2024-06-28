package org.acme.controlles;

import java.util.List;
import org.acme.entities.City;
import org.acme.entities.User;
import org.acme.entities.dto.LoginRequest;
import org.acme.enums.roles;
import org.acme.repositories.userRepository;
import org.acme.services.UserService;
import org.acme.services.cityService;
import org.acme.validation.MessageResponse;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("auth")

public class auth {

    @Inject
    userRepository userRepository;

    @Inject
    UserService userService;

    @Inject
    cityService cityService;

    @POST
    @Path("login")
    // @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response login(LoginRequest loginRequest) {
        User foundUser = userRepository.findByEmail(loginRequest.getEmail());
        if (foundUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new MessageResponse(false, "User not found"))
                    .build();
        }

        if (!BCrypt.verifyer().verify(loginRequest.getPassword().toCharArray(), foundUser.getPassword()).verified) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new MessageResponse(false, "Invalid password"))
                    .build();
        }

        String token = userService.generateToken(foundUser);

        return Response.ok(new MessageResponse(true, "Login successful", token)).build();
    }

    @POST
    @Path("register")
    @PermitAll
    @Transactional
    public Response register(User user) {
        try {
            userService.validateUser(user);

            userService.assignRole(user);

            City city = cityService.getCity(user.getCity().getId());

            User tmp = userService.createUserFromRequest(user, city);

            User createdUser = userService.createUser(tmp);

            String token = userService.generateToken(createdUser);

            if (token != null) {
                return Response.ok(new MessageResponse(true, "Registration successful", token)).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new MessageResponse(false, "Registration failed", e.getMessage())).build();
        }
    }

    @POST
    @Path("logout")
    @PermitAll
    public Response logout() {
        return Response.ok(new MessageResponse(true, "Logout successful")).build();
    }

    /*
     * @GET
     * 
     * @Path("role")
     * 
     * @PermitAll
     * public Response getroleByuser(@QueryParam("id") Long id) {
     * User user = userService.getUserById(id);
     * 
     * if (user != null) {
     * return Response.ok(user.getRole()).build();
     * } else {
     * return Response.status(Response.Status.NOT_FOUND).build();
     * }
     * }
     */

}