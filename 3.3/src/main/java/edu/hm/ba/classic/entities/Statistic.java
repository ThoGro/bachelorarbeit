package edu.hm.ba.classic.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Statistic {

    @Id
    @Column(name = "StatisticID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private int count;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Category category;

    public Statistic() {}

}
