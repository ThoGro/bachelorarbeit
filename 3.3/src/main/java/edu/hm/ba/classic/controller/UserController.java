package edu.hm.ba.classic.controller;

import edu.hm.ba.classic.entities.User;
import edu.hm.ba.classic.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
     * Returns the active user.
     * @param authentication authentication object containing the active user
     * @return active user.
     */
    @GetMapping("/user")
    public ResponseEntity<User> getActiveUser(Authentication authentication) {
        return ResponseEntity.ok(userService.getUser(authentication));
    }

}
