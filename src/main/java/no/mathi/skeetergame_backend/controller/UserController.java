package no.mathi.skeetergame_backend.controller;

import no.mathi.skeetergame_backend.model.User;
import no.mathi.skeetergame_backend.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user/")
public class UserController {

    @Autowired
    private UserService userService;

    //logger
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(UserController.class);

    //Get user by username
    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return userService.getUser(username);
    }

    //Update user
    @PostMapping("/update")
    public void updateUser(@RequestBody User user) {
        userService.updateUser(user);
    }

    //Get bio
    @GetMapping("/bio/{username}")
    public ResponseEntity<String> getBio(@PathVariable String username) {
        String bio = userService.getBio(username);
        if (bio.startsWith("{") && bio.endsWith("}")) {
            //trim the bio string to include everything after :"
            bio = bio.substring(bio.indexOf("\":") + 2, bio.length() - 2);
            bio = bio.substring(1);
            return ResponseEntity.ok(bio);
        }
        return ResponseEntity.ok(bio);
    }


    //Set bio
    @PostMapping("/bio/{username}")
    public void setBio(@PathVariable String username, @RequestBody String bio) {
        userService.setBio(username, bio);
    }

    //Set profile picture
    @PostMapping(value = "/profilePicture/{username}", consumes = "multipart/form-data")
    public ResponseEntity<?> setProfilePicture(@PathVariable String username, @RequestParam("file") MultipartFile file) {
        String filename = username + "_profile.jpg";
        String uploadDirPath = System.getProperty("user.home") + "/skeetergame/uploads/";
        File uploadDir = new File(uploadDirPath);

        // Ensure the directory exists
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        File targetFile = new File(uploadDir, filename); // Full path including directory and filename

        try {
            file.transferTo(targetFile); // Save the file to the target location
            String fileUrl = "/uploads/" + filename; // URL to access the image

            // Update the user's profile picture URL in the database
            userService.setProfilePicture(username, fileUrl);

            return ResponseEntity.ok(Map.of("profilePictureUrl", fileUrl));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }
    }

    @PostMapping(value = "/profilePictureUrl/{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> setProfilePictureUrl(@PathVariable String username, @RequestBody Map<String, String> requestBody) {
        String imageUrl = requestBody.get("imageUrl");
        String filename = username + "_profile.jpg";
        String uploadDirPath = System.getProperty("user.home") + "/skeetergame/uploads/";

        try {
            Files.createDirectories(Paths.get(uploadDirPath));

            // Download and save the image from URL
            try (InputStream in = new URL(imageUrl).openStream()) {
                Files.copy(in, Paths.get(uploadDirPath + filename), StandardCopyOption.REPLACE_EXISTING);
            }

            String fileUrl = "/uploads/" + filename;
            userService.setProfilePicture(username, fileUrl);

            return ResponseEntity.ok(Map.of("profilePictureUrl", fileUrl));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save profile picture from URL");
        }
    }

    @PostMapping("/proxy/generate-image")
    public ResponseEntity<?> proxyGenerateImage(@RequestBody Map<String, String> body) {
        String apiUrl = "https://dall-e-main.replit.app/generate-image";
        RestTemplate restTemplate = new RestTemplate();
        try {
            log.info("Starting image generation...");
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, body, Map.class);
            log.info("Generated AI image successfully!");
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            log.error("Error connecting to AI API: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error connecting to AI API");
        }
    }

    //Get profile picture
    @GetMapping("/profilePicture/{username}")
    public String getProfilePicture(@PathVariable String username) {
        return userService.getProfilePicture(username);
    }
}
