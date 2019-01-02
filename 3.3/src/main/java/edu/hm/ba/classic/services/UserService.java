package edu.hm.ba.classic.services;

import edu.hm.ba.classic.entities.User;

import java.util.Collection;

/**
 * This interface represents the functionalities of the user service.
 * @author Thomas Großbeck
 */
public interface UserService {

    /**
     * Returns all users.
     * @return collection with all users
     */
    Collection<User> getUsers();

    /**
     * Adds a user to the service.
     * @param user the user to add
     */
    void addUser(User user);

}
