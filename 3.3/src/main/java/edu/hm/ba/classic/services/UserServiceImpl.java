package edu.hm.ba.classic.services;

import edu.hm.ba.classic.entities.User;
import edu.hm.ba.classic.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Implementation of the UserService.
 * @author Thomas Großbeck
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            return null;
        }
        return new org.springframework.security.core.userdetails.User(username, "{noop}" + user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getRole().toString()));
    }
}
