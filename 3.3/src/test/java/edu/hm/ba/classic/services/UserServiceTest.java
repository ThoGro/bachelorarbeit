package edu.hm.ba.classic.services;

import edu.hm.ba.classic.entities.Role;
import edu.hm.ba.classic.entities.User;
import edu.hm.ba.classic.persistence.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    private static final User USER = new User(2, "Mitarbeiter", "mitarbeiter", Role.EMPLOYEE);

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService serviceUnderTest;

    @Test
    public void testGetUser() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(USER.getUsername());
        when(userRepository.findUserByUsername(USER.getUsername())).thenReturn(USER);
        User user = serviceUnderTest.getUser(authentication);
        assertEquals(USER, user);
    }

}
