package edu.hm.ba.serverless.model;

import lombok.*;

/**
 * Represents a book.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    /**
     * ISBN number of the book.
     */
    private String isbn;

    /**
     * Title of the book.
     */
    private String title;

    /**
     * Author of the book.
     */
    private String author;

    /**
     * Category of the book.
     */
    private Category category;

    /**
     * The User who lends the book. "null" if the book is available.
     */
    private String lender;

}
