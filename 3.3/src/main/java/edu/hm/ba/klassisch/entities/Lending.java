package edu.hm.ba.klassisch.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class Lending implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LendingID")
    private int id;
    @ManyToOne
    private User user;
    @OneToOne
    private Book book;

    public Lending() {
    }

    public Lending(User user, Book book) {
        this.user = user;
        this.book = book;
    }
    
}
