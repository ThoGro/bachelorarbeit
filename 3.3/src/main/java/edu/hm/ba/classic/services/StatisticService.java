package edu.hm.ba.classic.services;

import edu.hm.ba.classic.entities.Statistic;

import java.util.Collection;

public interface StatisticService {

    void count(String category);

    Collection<Statistic> getStatistics();

    Statistic getStatistic(String category);

}
