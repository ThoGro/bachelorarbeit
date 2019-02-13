package edu.hm.ba.classic.services;

import edu.hm.ba.classic.entities.Statistic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class StatisticServiceIntegrationTest {

    @Autowired
    private StatisticService statisticService;

    @Test
    public void testGetStatistics() {
        Collection<Statistic> statistics = statisticService.getStatistics();
        assertThat(statistics).isNotNull().isNotEmpty();
    }

    @Test
    public void testGetStatistic() {
        Statistic statistic = statisticService.getStatistic("SCIENCE");
        assertThat(statistic).isNotNull();
    }

    @Test
    public void testCount() {

    }

}
