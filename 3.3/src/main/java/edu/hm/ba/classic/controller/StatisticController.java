package edu.hm.ba.classic.controller;

import edu.hm.ba.classic.entities.Statistic;
import edu.hm.ba.classic.services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * The class provides endpoints for the communication with the statistic service.
 * @author Thomas Großbeck
 */
@RestController
@CrossOrigin
public class StatisticController {

    @Autowired
    StatisticService statisticService;

    /**
     * Increments the counter for the statistic with the specified category.
     * @param category the category of the statistic to increment
     */
    @PostMapping(path = "/statistic/{category}")
    public void count(@PathVariable String category) {
        statisticService.count(category);
    }

    /**
     * Lists all statistics.
     * @return response with the status and a collection with all statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<Collection<Statistic>> getStatistics() {
        return ResponseEntity.ok(statisticService.getStatistics());
    }

    /**
     * Lists a specific statistic.
     * @param category the category from the required statistic
     * @return response with the status and the statistic for the specified category
     */
    @GetMapping("/statistic/{category}")
    public ResponseEntity<Statistic> getStatistic(@PathVariable String category) {
        return ResponseEntity.ok(statisticService.getStatistic(category));
    }

}
