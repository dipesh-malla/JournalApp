package malla.dipesh.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import malla.dipesh.journalApp.exception.UserNotFound;
import malla.dipesh.journalApp.model.User;
import malla.dipesh.journalApp.repository.JournalEntryRepository;
import malla.dipesh.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public UserService(UserRepository userRepository, JournalEntryRepository journalEntryRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.journalEntryRepository = journalEntryRepository;

    }

    public void saveNewUser(User user) {
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(List.of("USER"));
            userRepository.save(user);
        }catch (Exception e){
            log.warn("Failed to save user {}: " , e.getMessage());
            throw new RuntimeException("Failed to save user");
        }
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(ObjectId id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteById(ObjectId id) {
        userRepository.deleteById(id);
    }

    public User updateByUsername(String username, User newUser) {
        User old = userRepository.findByUsername((username)).orElseThrow(() -> new RuntimeException("Username already exists"));
        if(old == null){
            return null;
        }
        old.setUsername(!newUser.getUsername().isEmpty() ?newUser.getUsername():old.getUsername());
        old.setPassword(!newUser.getPassword().isEmpty() ?newUser.getPassword():old.getPassword());
        return userRepository.save(old);
    }


    public User findByUserName(String username) {
       return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFound("User not found"));
    }

    public void saveAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("ADMIN"));
        userRepository.save(user);
    }
}
