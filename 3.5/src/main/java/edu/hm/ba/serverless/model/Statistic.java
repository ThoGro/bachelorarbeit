package edu.hm.ba.serverless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a statistic. A statistic counts the lendings for all books with the same category.
 * For each category exists one statistic.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Statistic {

    /**
     * The count of the statistic. Is incremented for each lend with the category.
     */
    private int statisticCount;

    /**
     * Category for which the statistic is.
     */
    private Category category;

}
