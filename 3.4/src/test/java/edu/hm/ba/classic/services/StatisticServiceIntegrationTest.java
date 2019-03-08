package edu.hm.ba.classic.services;

import edu.hm.ba.classic.entities.Category;
import edu.hm.ba.classic.entities.Statistic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class StatisticServiceIntegrationTest {

    @Autowired
    private StatisticService statisticService;

    @Test
    public void testGetStatistics() {
        Collection<Statistic> statistics = statisticService.getStatistics();
        assertThat(statistics).isNotNull().isNotEmpty();
        assertEquals(3, statistics.size());
    }

    @Test
    public void testGetStatistic() {
        Statistic statistic = statisticService.getStatistic("SCIENCE");
        assertThat(statistic).isNotNull();
        assertEquals(Category.SCIENCE, statistic.getCategory());
    }

    @Test
    public void testCount() {
        Statistic statistic = statisticService.count(Category.SCIENCE);
        assertThat(statistic).isNotNull();
        assertThat(statistic.getStatisticCount()).isGreaterThanOrEqualTo(1);
    }

}
