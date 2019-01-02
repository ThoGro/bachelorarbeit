package edu.hm.ba.classic.services;

import edu.hm.ba.classic.entities.User;
import edu.hm.ba.classic.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Implementation of the UserService.
 * @author Thomas Großbeck
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * User Repository.
     */
    @Autowired
    UserRepository userRepository;

    @Override
    public Collection<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }
}
