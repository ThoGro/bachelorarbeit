package edu.hm.ba.classic.services;

import edu.hm.ba.classic.entities.User;
import org.springframework.security.core.Authentication;

/**
 * This interface represents the functionalities of the user service.
 * @author Thomas Großbeck
 */
public interface UserService {

    /**
     * Returns the active user.
     * @param authentication authentication object containing the active user
     * @return the active user
     */
    User getUser(Authentication authentication);

}
