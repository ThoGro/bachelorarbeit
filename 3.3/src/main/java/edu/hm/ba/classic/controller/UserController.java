package edu.hm.ba.classic.controller;

import edu.hm.ba.classic.entities.User;
import edu.hm.ba.classic.persistence.UserRepository;
import edu.hm.ba.classic.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public ResponseEntity<Collection<User>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @PostMapping(path = "/user", consumes = "application/json")
    public void addUser(@RequestBody User user) {
        userService.addUser(user);
    }

}
