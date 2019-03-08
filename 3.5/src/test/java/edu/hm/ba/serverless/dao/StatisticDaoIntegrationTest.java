package edu.hm.ba.serverless.dao;

import edu.hm.ba.serverless.model.Category;
import edu.hm.ba.serverless.model.Statistic;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class StatisticDaoIntegrationTest {

    private static final Statistic STATISTIC = new Statistic(12, Category.HISTORY);

    private static final String TABLE_NAME = "statistics";

    private StatisticDao statisticDao;

    private Statistic statistic;

    @Before
    public void setUp() {
        DynamoDbClient dynamoDb = DynamoDbClient.builder()
                .region(Region.EU_CENTRAL_1)
                .build();
        statisticDao = new StatisticDao(dynamoDb, TABLE_NAME);
        statistic = statisticDao.getStatistic(STATISTIC.getCategory());
    }

    @After
    public void cleanUp() {
        statisticDao.createStatistic(statistic);
    }

    @Test
    public void testGetStatistic() {
        Statistic statistic = statisticDao.getStatistic(STATISTIC.getCategory());
        assertThat(statistic).isNotNull();
        assertEquals(STATISTIC.getCategory(), statistic.getCategory());
    }

    @Test
    public void testGetStatistics(){
        List<Statistic> statistics = statisticDao.getStatistics();
        assertThat(statistics).isNotNull();
        assertEquals(3, statistics.size());
    }

    @Test
    public void testCreateStatistic() {
        Statistic statistic = statisticDao.createStatistic(new Statistic(STATISTIC.getStatisticCount(), STATISTIC.getCategory()));
        assertThat(statistic).isNotNull();
        assertEquals(STATISTIC, statistic);
    }

    @Test
    public void testCount() {
        Statistic statistic = statisticDao.count(STATISTIC.getCategory());
        assertThat(statistic).isNotNull();
        assertThat(statistic.getStatisticCount()).isGreaterThanOrEqualTo(1);
    }

}
