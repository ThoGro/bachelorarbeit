package edu.hm.ba.klassisch.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
