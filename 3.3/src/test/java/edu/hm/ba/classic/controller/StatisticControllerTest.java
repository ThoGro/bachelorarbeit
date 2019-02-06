package edu.hm.ba.classic.controller;

import edu.hm.ba.classic.entities.Category;
import edu.hm.ba.classic.entities.Statistic;
import edu.hm.ba.classic.services.StatisticService;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(StatisticController.class)
@WebAppConfiguration
public class StatisticControllerTest {

    private static final Collection<Statistic> STATISTICS = new ArrayList<>(Arrays.asList(
            new Statistic(1, 23, Category.SCIENCE),
            new Statistic(1, 12, Category.HISTORY),
            new Statistic(1, 55, Category.FANTASY)
    ));

    private static final Statistic STATISTIC = new Statistic(1, 34, Category.SCIENCE);

    @MockBean
    private StatisticService statisticService;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private WebApplicationContext webAppContext;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void testCount() {

    }

    @Test
    @WithMockUser(authorities = "EMPLOYEE")
    public void testGetStatistics() throws Exception {
        when(statisticService.getStatistics()).thenReturn(STATISTICS);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/statistics");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("[{\"id\":1,\"count\":23,\"category\":\"SCIENCE\"},{\"id\":1,\"count\":12,\"category\":\"HISTORY\"},{\"id\":1,\"count\":55,\"category\":\"FANTASY\"}]",
                result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(authorities = "CUSTOMER")
    public void testGetStatisticsAccessDenied() throws Exception {
        exception.expectCause(IsInstanceOf.<Throwable>instanceOf(AccessDeniedException.class));
        when(statisticService.getStatistics()).thenReturn(STATISTICS);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/statistics");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    }

    @Test
    @WithMockUser(authorities = "EMPLOYEE")
    public void testGetStatistic() throws Exception {
        when(statisticService.getStatistic("SCIENCE")).thenReturn(STATISTIC);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/statistics/SCIENCE");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"id\":1,\"count\":34,\"category\":\"SCIENCE\"}", result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(authorities = "CUSTOMER")
    public void testGetStatisticAccessDenied() throws Exception {
        exception.expectCause(IsInstanceOf.<Throwable>instanceOf(AccessDeniedException.class));
        when(statisticService.getStatistic("SCIENCE")).thenReturn(STATISTIC);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/statistics/SCIENCE");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    }

}
