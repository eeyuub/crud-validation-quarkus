package org.acme.security;

//import java.security.Principal;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
//import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import java.util.HashSet;
import org.eclipse.microprofile.jwt.JsonWebToken;
import io.smallrye.jwt.build.Jwt;
import java.util.Arrays;

@Path("/secured")
public class TokenSecuredResource {

    @Inject
    JsonWebToken jwt;

    @GET()
    @Path("permit-all")
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@Context SecurityContext ctx) {
        return getResponseString(ctx);
    }

    @GET
    @Path("roles-allowed")
    @RolesAllowed({ "User", "Admin" })
    @Produces(MediaType.TEXT_PLAIN)
    public String helloRolesAllowed(@Context SecurityContext ctx) {
        return getResponseString(ctx) + ", birthdate: " + jwt.getClaim("birthdate").toString() + ", name: " + jwt.getClaim("name").toString() + ", email: " + jwt.getClaim("email").toString() + ", password: " + jwt.getClaim("password").toString();
    }

    @GET
    @Path("generate-token")
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public String generateToken() {
        
        String token = Jwt.issuer("https://localhost/issuer")
                .upn("jdoe@quarkus.io")
                .groups(new HashSet<>(Arrays.asList("User", "Admin")))
                .claim("birthdate", "2001-07-13")
                .claim("name", "John Doe")
                .claim("email", "jdoe@quarkus.io")
                .claim("password", "password")
                .sign();
        return token;
    }

    private String getResponseString(SecurityContext ctx) {
        String name;
        if (ctx.getUserPrincipal() == null) {
            name = "anonymous";
        } else if (!ctx.getUserPrincipal().getName().equals(jwt.getName())) {
            throw new InternalServerErrorException("Principal and JsonWebToken names do not match");
        } else {
            name = ctx.getUserPrincipal().getName();
        }
        return String.format("hello " + "%s,"
                + " isHttps: " + "%s,"
                + " authScheme: " + "%s,"
                + " hasJWT: " + "%s",
                name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJwt());
    }

    private boolean hasJwt() {
        return jwt.getClaimNames() != null;
    }
}