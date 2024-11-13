package no.mathi.skeetergame_backend.controller;

import lombok.extern.slf4j.Slf4j;
import no.mathi.skeetergame_backend.model.User;
import no.mathi.skeetergame_backend.repo.UserRepo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
public class AuthController {

    @Autowired
    private UserRepo userRepository;

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> loginData) {
        // Get the username and password from the request body
        String username = loginData.get("username");
        String password = loginData.get("password");

        // Find the user by username
        Optional<User> user = userRepository.findByUsername(username);

        // Check if the user exists and the password is correct
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            // Return a success message
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            log.info("Login successful - USER: " + username);
            return response;
        } else {
            // Return an error message
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid username or password");
            log.info("Invalid username or password - login failed - USER: " + username);
            return response;
        }
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> registerData) {
        // Get the username and password from the request body
        String username = registerData.get("username");
        String password = registerData.get("password");

        // Check if the username is already taken
        if (userRepository.findByUsername(username).isPresent()) {
            // Return an error message
            Map<String, String> response = new HashMap<>();
            response.put("message", "Username is already taken");
            log.info("Username is already taken - USER: " + username);
            return response;
        } else {
            // Create a new user
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);

            // Save the user to the database
            userRepository.save(user);

            // Return a success message
            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully");
            log.info("User registered successfully - USER: " + username);
            return response;
        }
    }
}
