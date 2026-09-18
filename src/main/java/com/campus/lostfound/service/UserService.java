package com.campus.lostfound.service;

import com.campus.lostfound.model.Role;
import com.campus.lostfound.model.User;
import com.campus.lostfound.util.AppLogger;
import com.campus.lostfound.util.Security;
import com.campus.lostfound.util.Validate;

public class UserService {

    private DataStore database;

    public UserService(DataStore database) {
        this.database = database;
    }

    // Register a new user
    public User register(
            String id,
            String name,
            String password,
            Role role) {

        Validate.requiredtext(id);
        Validate.requiredtext(name);
        Validate.requiredtext(password);

        if (role == null) {
            throw new IllegalArgumentException("Role is required.");
        }

        id = id.trim();
        name = name.trim();

        // Check if the user ID is already registered
        if (database.getUsers().containsKey(id)) {
            throw new IllegalArgumentException(
                    "User ID already exists."
            );
        }

        // Store password in hashed form
        String passwordHash = Security.hashPassword(password);

        User newUser = new User(
                id,
                name,
                passwordHash,
                role
        );

        database.getUsers().put(id, newUser);
        database.saveUsers();

        AppLogger.info("New user registered: " + id);

        return newUser;
    }

    // Login an existing user
    public User Login(
            String id,
            String password) {

        Validate.requiredtext(id);
        Validate.requiredtext(password);

        id = id.trim();

        User user = database.getUsers().get(id);

        if (user == null) {
            throw new IllegalArgumentException(
                    "User ID or password is incorrect."
            );
        }

        // Hash the entered password and compare it
        String enteredPassword =
                Security.hashPassword(password);

        if (!user.getPasswordHash().equals(enteredPassword)) {
            throw new IllegalArgumentException(
                    "User ID or password is incorrect."
            );
        }

        AppLogger.info("User logged in: " + id);

        return user;
    }
}