package edu.hm.ba.classic.services;

import edu.hm.ba.classic.entities.User;
import edu.hm.ba.classic.persistence.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Implementation of the UserService.
 * @author Thomas Großbeck
 */
@Service
@Transactional(rollbackOn = { Exception.class })
public class UserServiceImpl implements UserService, UserDetailsService {

    /**
     * User Repository.
     */
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            return null;
        }
        return new org.springframework.security.core.userdetails.User(username, "{noop}" + user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getRole().toString()));
    }

    @Override
    public User getUser(Authentication authentication) {
        return userRepository.findUserByUsername(authentication.getName());
    }
}
