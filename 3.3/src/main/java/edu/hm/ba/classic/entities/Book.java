package edu.hm.ba.classic.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Represents a book in the library service.
 * @author Thomas Großbeck
 */
@Data
@Entity
public class Book {

    /**
     * ISBN number of the book.
     */
    @Id
    private String isbn;

    /**
     * Title of the book.
     */
    @NotNull
    private String title;

    /**
     * Author of the book.
     */
    @NotNull
    private String author;

    /**
     * Category of the book.
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    private Category category;

    /**
     * The User who lends the book. Null if the book is available.
     */
    @ManyToOne
    private User lender;

    /**
     * Default constructor.
     */
    public Book() {}

    /**
     * Constructs a book with isbn, title, author and category.
     * @param isbn the isbn of the book
     * @param title the title of the book
     * @param author the author of the book
     * @param category the category of the book
     */
    public Book(String isbn, String title, String author, Category category) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
    }

}
