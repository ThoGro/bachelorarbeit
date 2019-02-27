package edu.hm.ba.classic.controller;

import edu.hm.ba.classic.entities.Role;
import edu.hm.ba.classic.entities.User;
import edu.hm.ba.classic.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserControllerTest {

    private static final User USER = new User(2, "Mitarbeiter", "mitarbeiter", Role.EMPLOYEE);

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext webAppContext;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void testGetActiveUser() throws Exception {
        when(userService.getUser(null)).thenReturn(USER);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"id\":2,\"username\":\"Mitarbeiter\",\"password\":\"mitarbeiter\",\"role\":\"EMPLOYEE\"}",
                result.getResponse().getContentAsString());
    }

}
