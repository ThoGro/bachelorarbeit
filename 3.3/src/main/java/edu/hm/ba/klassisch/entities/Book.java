package edu.hm.ba.klassisch.entities;

import javax.persistence.*;
import java.io.Serializable;

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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Lending getLending() {
        return lending;
    }

    public void setLending(Lending lending) {
        this.lending = lending;
    }
}
