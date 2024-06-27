package org.acme.services;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.acme.entities.User;

@ApplicationScoped
public class UserService {
    private Map<String, User> users = new HashMap<>();

    public User createUser(String name, String email, String password) {
        String id = UUID.randomUUID().toString();
        String encryptedPassword = encryptPassword(password);
        User user = new User( name, email, encryptedPassword);
        users.put(id, user);
        return user;
    }

    public Optional<User> getUserById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    public Optional<User> getUserByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    public User updateUser(String id, String name, String email, String password) {
        User user = users.get(id);
        if (user != null) {
            user.setName(name);
            user.setEmail(email);
            user.setPassword(encryptPassword(password));
        }
        return user;
    }

    public void deleteUser(String id) {
        users.remove(id);
    }

    private String encryptPassword(String password) {
        // Implement password encryption logic here
        return password; // Placeholder, replace with actual encryption
    }
}