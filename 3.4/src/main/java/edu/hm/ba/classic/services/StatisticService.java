package edu.hm.ba.classic.services;

import edu.hm.ba.classic.entities.Category;
import edu.hm.ba.classic.entities.Statistic;

import java.util.Collection;

/**
 * This interface represents the functionalities of the statistic service.
 * @author Thomas Großbeck
 */
public interface StatisticService {

    /**
     * Increments the statistic counter for the specified category.
     * @param category the category for which the counter is incremented
     * @return the incremented statistic
     */
    Statistic count(Category category);

    /**
     * Returns all statistics.
     * @return collection with all statistics
     */
    Collection<Statistic> getStatistics();

    /**
     * Returns the statistic for the specified category.
     * @param category the category for which the statistic will be returned
     * @return the statistic for the specified category
     */
    Statistic getStatistic(String category);

}
