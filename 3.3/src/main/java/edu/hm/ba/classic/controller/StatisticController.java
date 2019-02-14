package edu.hm.ba.classic.controller;

import edu.hm.ba.classic.entities.Category;
import edu.hm.ba.classic.entities.Statistic;
import edu.hm.ba.classic.services.StatisticService;
import org.hibernate.stat.Statistics;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * The class provides endpoints for the communication with the statistic service.
 * @author Thomas Großbeck
 */
@RestController
@CrossOrigin
public class StatisticController {

    private StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    /**
     * Increments the counter for the statistic with the specified category.
     * @param category the category of the statistic to increment
     * @return response with the status and the incremented statistic
     */
    @PostMapping(path = "/statistics/{category}")
    public ResponseEntity<Statistic> count(@PathVariable String category) {
        return ResponseEntity.ok(statisticService.count(Category.valueOf(category)));
    }

    /**
     * Lists all statistics.
     * @return response with the status and a collection with all statistics
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE')")
    public ResponseEntity<Collection<Statistic>> getStatistics() {
        return ResponseEntity.ok(statisticService.getStatistics());
    }

    /**
     * Lists a specific statistic.
     * @param category the category from the required statistic
     * @return response with the status and the statistic for the specified category
     */
    @GetMapping("/statistics/{category}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE')")
    public ResponseEntity<Statistic> getStatistic(@PathVariable String category) {
        return ResponseEntity.ok(statisticService.getStatistic(category));
    }

}
