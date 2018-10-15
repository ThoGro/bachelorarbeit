package edu.hm.ba.klassisch.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Entity
public class Book implements Serializable {

    @Id
    private String isbn;
    private String title;
    private String author;
    @OneToOne(mappedBy = "book")
    private Lending lending;

}
