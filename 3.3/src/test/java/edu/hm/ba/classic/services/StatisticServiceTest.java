package edu.hm.ba.classic.services;

import edu.hm.ba.classic.entities.Category;
import edu.hm.ba.classic.entities.Statistic;
import edu.hm.ba.classic.persistence.StatisticRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class StatisticServiceTest {

    private static final Statistic STATISTIC = new Statistic(1, 12, Category.SCIENCE);

    private static final List<Statistic> STATISTICS = new ArrayList<>(Arrays.asList(
            STATISTIC,
            new Statistic(2, 234, Category.HISTORY),
            new Statistic(3, 74, Category.FANTASY)
    ));

    @MockBean
    private StatisticRepository statisticRepository;

    @Autowired
    private StatisticService serviceUnderTest;

    @Test
    public void testGetStatistic() {
        when(statisticRepository.getStatisticByCategory(Category.SCIENCE)).thenReturn(STATISTIC);
        Statistic statistic = serviceUnderTest.getStatistic("SCIENCE");
        assertEquals(STATISTIC, statistic);
    }

    @Test
    public void testGetStatistics() {
        when(statisticRepository.findAll()).thenReturn(STATISTICS);
        Collection<Statistic> statistics = serviceUnderTest.getStatistics();
        assertEquals(STATISTICS, statistics);
    }

    @Test
    public void testCount() {
        
    }

}
