package edu.hm.ba.klassisch.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Lending implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private User user;
    @OneToOne
    private Book book;
}
