package org.acme.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.JsonbBuilder;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.validation.ConstraintViolationException;
import org.acme.entities.City;
import org.acme.entities.User;
import org.acme.enums.roles;
import org.acme.repositories.userRepository;
import org.acme.validation.MessageResponse;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.smallrye.jwt.build.Jwt;

@ApplicationScoped
public class UserService {

    @Inject
    private userRepository userRepository;

    @Inject
    Validator validator;

    public PanacheQuery<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User entry) {
        String encryptedPassword = encryptPassword(entry.getPassword());
        User user = new User(entry.getName(),
                entry.getRole(),
                entry.getUsername(),
                entry.getEmail(),
                encryptedPassword,
                entry.getCity());
        userRepository.persist(user);
        return user;
    }

    public User getUserById(long id) {
        return userRepository.findById(id);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(long id, User user) {
        User userToUpdate = userRepository.findById(id);
        if (user != null) {
            userToUpdate.setName(user.getName());
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setPassword(user.getPassword());
            userToUpdate.setCity(user.getCity());
            userRepository.persist(userToUpdate); // Ensure this method persists the changes
        }
        return userToUpdate;
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    private String encryptPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public String generateToken(User user) {
        /*
         * User foundUser = userRepository.findByEmail(user.getEmail());
         * if (foundUser != null &&
         * BCrypt.verifyer().verify(user.getPassword().toCharArray(),
         * foundUser.getPassword()).verified) {
         */
        String token = Jwt.issuer("https://localhost/issuer")
                .upn(user.getEmail())
                .groups(new HashSet<>(Arrays.asList("User", "Admin")))
                .claim("username", user.getUsername())
                .claim("role", user.getRole())
                .claim("city", user.getCity().getName())
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .expiresIn(Duration.ofMinutes(1))
                .sign();
        return token;
        /*
         * }
         * return null;
         */
    }

    public void validateUser(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        if (!violations.isEmpty()) {
            List<String> errorMessages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            throw new NotFoundException(String.join(", ", errorMessages));
        }
    }

    public void assignRole(User user) {
        if (user.getRole() == null) {
            throw new NotFoundException("Role is required");
        }
        switch (user.getRole()) {
            case ADMIN:
                user.setRole(roles.ADMIN);
                break;
            case USER:
                user.setRole(roles.USER);
                break;
            default:
                break;
            // throw new NotFoundException("Role not found");
        }
    }

    public User createUserFromRequest(User user, City city) {
        User tmp = new User();
        tmp.setName(user.getName());
        tmp.setEmail(user.getEmail());
        tmp.setPassword(user.getPassword());
        tmp.setRole(user.getRole());
        tmp.setCity(city);
        tmp.setUsername(user.getUsername());
        return tmp;
    }
}