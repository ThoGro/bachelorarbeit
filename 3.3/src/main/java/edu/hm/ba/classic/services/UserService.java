package edu.hm.ba.classic.services;

import edu.hm.ba.classic.entities.User;

import java.util.Collection;

public interface UserService {

    Collection<User> getUsers();

    void addUser(User user);

}
