package edu.hm.ba.classic.controller;

import edu.hm.ba.classic.entities.Statistic;
import edu.hm.ba.classic.services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin
public class StatisticController {

    @Autowired
    StatisticService statisticService;

    @PostMapping(path = "/statistic/{category}")
    public void count(@PathVariable String category) {
        statisticService.count(category);
    }

    @GetMapping("/statistics")
    public ResponseEntity<Collection<Statistic>> getStatistics() {
        return ResponseEntity.ok(statisticService.getStatistics());
    }

    @GetMapping("/statistic/{category}")
    public ResponseEntity<Statistic> getStatistic(@PathVariable String category) {
        return ResponseEntity.ok(statisticService.getStatistic(category));
    }

}
