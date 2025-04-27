package malla.dipesh.journalApp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import malla.dipesh.journalApp.api.response.WeatherResponse;
import malla.dipesh.journalApp.model.User;
import malla.dipesh.journalApp.service.EmailService;
import malla.dipesh.journalApp.service.UserRepositoryImpl;
import malla.dipesh.journalApp.service.UserService;
import malla.dipesh.journalApp.service.WeatherService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "User APIs", description = "all about user endpoints") // for swagger
public class UserController {

    private final UserRepositoryImpl userRepositoryImpl;
    private final UserService userService;
    private final WeatherService weatherService;
    private final EmailService emailService;

    public UserController(UserRepositoryImpl userRepositoryImpl, UserService userService, WeatherService weatherService, EmailService emailService) {
        this.userRepositoryImpl = userRepositoryImpl;
        this.userService = userService;
        this.weatherService = weatherService;
        this.emailService = emailService;
    }


    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody User user) {
        try {
            userService.saveNewUser(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/getYourself")
    public ResponseEntity<?> findById() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUserName(username);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ObjectId userId = userService.findByUserName(username).getId();
        userService.deleteById(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateById(

           @Validated @RequestBody User newUser
    ) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
           userService.updateByUsername(username, newUser);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{city}")
    public ResponseEntity<?> greeting(@PathVariable String city) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        WeatherResponse weatherResponse = weatherService.getCurrentWeather(city);
        String greeting = "";
        if(weatherResponse != null) {
            greeting = "Weather feels like "+ weatherResponse.getCurrent().getFeelslike();
        }
        return new ResponseEntity<>("Hello " + username + ", Weather feels like " + greeting, HttpStatus.OK);
    }

    @GetMapping("/sentiment")
    public List<User> sentiment(){
        return userRepositoryImpl.getUserForSA();
    }

    @PostMapping("/sendmail")
    public ResponseEntity<?> sendMail(){
        try {
            emailService.sendMail("novaluna1913@gmail.com", "Test mail", "This is a test mail");
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
