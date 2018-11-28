package edu.hm.ba.classic.services;

import edu.hm.ba.classic.entities.Category;
import edu.hm.ba.classic.entities.Statistic;
import edu.hm.ba.classic.persistence.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    StatisticRepository statisticRepository;

    @Override
    public void count(String category) {
        Statistic statistic = statisticRepository.getStatisticByCategory(Category.valueOf(category));
        int newCount = statistic.getCount() + 1;
        statistic.setCount(newCount);
        statisticRepository.save(statistic);
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
