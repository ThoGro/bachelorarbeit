package edu.hm.ba.classic.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Represents a statistic. A statistic counts the lendings for all books with the same category.
 * For each category exists one statistic.
 * @author Thomas Großbeck
 */
@Data
@Entity
public class Statistic {

    /**
     * ID of the statistic.
     */
    @Id
    @Column(name = "StatisticID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The count of the statistic. Is incremented for each lend with the category.
     */
    @NotNull
    private int statisticCount;

    /**
     * Category for which the statistic is
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    private Category category;

    /**
     * Default constructor.
     */
    public Statistic() {}

    /**
     * Constructs a statistic with id, count and category.
     * @param id the id of the statistic
     * @param count the count of the statistic
     * @param category the category of the statistic
     */
    public Statistic(int id, int statisticCount, Category category) {
        this.id = id;
        this.statisticCount = statisticCount;
        this.category = category;
    }

}
