package edu.hm.ba.classic.persistence;

import edu.hm.ba.classic.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the user.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Returns a user for the specified username.
     * @param username name for which the user is required
     * @return the user with the specified username
     */
    User findUserByUsername(String username);

}
