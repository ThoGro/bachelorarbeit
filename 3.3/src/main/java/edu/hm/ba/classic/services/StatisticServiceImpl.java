package edu.hm.ba.classic.services;

import edu.hm.ba.classic.entities.Category;
import edu.hm.ba.classic.entities.Statistic;
import edu.hm.ba.classic.persistence.StatisticRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;

/**
 * Implementation of the StatisticService.
 * @author Thomas Großbeck
 */
@Service
@Transactional(rollbackOn = { Exception.class })
public class StatisticServiceImpl implements StatisticService {

    /**
     * Statistic Repository.
     */
    private StatisticRepository statisticRepository;

    public StatisticServiceImpl(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }

    @Override
    public Statistic count(Category category) {
        Statistic statistic = statisticRepository.getStatisticByCategory(category);
        int newCount = statistic.getCount() + 1;
        statistic.setCount(newCount);
        return statisticRepository.save(statistic);
    }

    @Override
    public Collection<Statistic> getStatistics() {
        return statisticRepository.findAll();
    }

    @Override
    public Statistic getStatistic(String category) {
        return statisticRepository.getStatisticByCategory(Category.valueOf(category));
    }
}
