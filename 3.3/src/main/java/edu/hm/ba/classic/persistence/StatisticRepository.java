package edu.hm.ba.classic.persistence;

import edu.hm.ba.classic.entities.Category;
import edu.hm.ba.classic.entities.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Integer> {

    /**
     * Returns a statistic for the specified category.
     * @param category the category for which the statistic is required
     * @return returns the statistic for the specified category
     */
    Statistic getStatisticByCategory(Category category);

}
