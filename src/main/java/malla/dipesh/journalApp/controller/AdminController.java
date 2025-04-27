package malla.dipesh.journalApp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import malla.dipesh.journalApp.cache.AppCache;
import malla.dipesh.journalApp.model.User;
import malla.dipesh.journalApp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin APIs", description = "all about admin endpoints") // for swagger

public class AdminController {

    private final UserService userService;
    private final AppCache appCache;

    public AdminController(UserService userService, AppCache appCache) {
        this.userService = userService;
        this.appCache = appCache;
    }

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers(){
        List<User> allUsers = userService.findAll();
        if (!allUsers.isEmpty()) {
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping("/create-admin-user")
    public ResponseEntity<?> createUser(@RequestBody User user){
        try{
            userService.saveAdmin(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/clear-app-cache")
    public void clearAppCache(){
        appCache.init();
    }

}
