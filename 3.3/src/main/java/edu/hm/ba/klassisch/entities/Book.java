package edu.hm.ba.klassisch.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class Book implements Serializable {

    @Id
    private String isbn;
    private String title;
    private String author;
    @OneToOne(mappedBy = "book")
    private Lending lending;

    public Book() {

    }

    public Book(String isbn, String title, String author, Lending lending) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.lending = lending;
    }

}
