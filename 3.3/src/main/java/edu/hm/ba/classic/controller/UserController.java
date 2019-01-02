package edu.hm.ba.classic.controller;

import edu.hm.ba.classic.entities.User;
import edu.hm.ba.classic.persistence.UserRepository;
import edu.hm.ba.classic.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * The class provides endpoints for the communication with the user service.
 * @author Thomas Großbeck
 */
@RestController
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    /**
     * Lists all users.
     * @return response with the status and a collection with all users
     */
    @GetMapping("/users")
    public ResponseEntity<Collection<User>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    /**
     * Creates a new user in the service.
     * @param user the new user
     */
    @PostMapping(path = "/user", consumes = "application/json")
    public void addUser(@RequestBody User user) {
        userService.addUser(user);
    }

}
