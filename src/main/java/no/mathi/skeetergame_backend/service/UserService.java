package no.mathi.skeetergame_backend.service;

import no.mathi.skeetergame_backend.model.User;
import no.mathi.skeetergame_backend.repo.UserRepo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService{

    @Autowired
    private UserRepo userRepository;

    //logger
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(UserService.class);

    public boolean isUsernameTaken(String username){
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean isPasswordCorrect(String username, String password){
        return userRepository.findByUsername(username)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

    public void registerUser(String username, String password){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userRepository.save(user);
    }

    public User getUser(String username){
        User tmp = userRepository.findByUsername(username).orElse(null);
        if (tmp != null){
            tmp.setPassword("REDACTED");
        }
        return tmp;
    }

    public void updateUser(User user){
        User tmp = userRepository.findById(user.getId()).orElse(null);

        if(tmp == null){
            return;
        }

        tmp.setBio(user.getBio());
        tmp.setId(user.getId());
        tmp.setProfilePicture(user.getProfilePicture());

        userRepository.save(tmp);
    }

    public void deleteUser(String username){
        userRepository.delete(userRepository.findByUsername(username).orElse(null));
    }

    public void deleteUser(User user){
        userRepository.delete(user);
    }

    //get user bio
    public String getBio(String username){
        return userRepository.findByUsername(username)
                .map(User::getBio)
                .orElse(null);
    }

    //get user profile picture
    public String getProfilePicture(String username){
        log.info("Getting profile picture for user: " + username);
        User tmp = userRepository.findByUsername(username).orElse(null);
        log.info("User found: " + tmp.getProfilePicture());
        if(tmp != null){
            return tmp.getProfilePicture();
        }
        return null;
    }

    //set user bio
    public void setBio(String username, String bio){
        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    user.setBio(bio);
                    userRepository.save(user);
                });
    }

    //set user profile picture
    public void setProfilePicture(String username, String profilePicture){
        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    user.setProfilePicture(profilePicture);
                    userRepository.save(user);
                });
    }
}
