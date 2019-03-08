package edu.hm.ba.classic.services;

import edu.hm.ba.classic.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    public void testGetUser() {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User("Mitarbeiter", "{noop}mitarbeiter",
                AuthorityUtils.createAuthorityList("EMPLOYEE"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "mitarbeiter", userDetails.getAuthorities());
        User user = userService.getUser(authentication);
        assertThat(user).isNotNull();
        assertEquals("Mitarbeiter", user.getUsername());
        assertEquals("mitarbeiter", user.getPassword());
    }

}
