package edu.hm.ba.classic.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class StatisticControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webAppContext;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    @WithMockUser(authorities = "EMPLOYEE")
    public void testGetStatistics() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/statistics");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("[{\"id\":1,\"statisticCount\":0,\"category\":\"HISTORY\"},{\"id\":2,\"statisticCount\":1,\"category\":\"SCIENCE\"},{\"id\":3,\"statisticCount\":0,\"category\":\"FANTASY\"}]",
                result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(authorities = "EMPLOYEE")
    public void testGetStatistic() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/statistics/SCIENCE");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"id\":2,\"statisticCount\":1,\"category\":\"SCIENCE\"}", result.getResponse().getContentAsString());
    }

    @Test
    public void testCount() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/statistics/SCIENCE");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"id\":2,\"statisticCount\":1,\"category\":\"SCIENCE\"}", result.getResponse().getContentAsString());
    }



}
