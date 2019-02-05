package edu.hm.ba.classic.services;

import edu.hm.ba.classic.entities.Book;

import java.util.Collection;

/**
 * This interface represents the functionalities of the book service.
 * @author Thomas Großbeck
 */
public interface BookService {

    /**
     * Returns all books.
     * @return collection with all books
     */
    Collection<Book> getBooks();

    /**
     * Adds a book to the service.
     * @param book the book to add
     * @return the added book
     */
    Book addBook(Book book);

    /**
     * Deletes a book from the service.
     * @param isbn the isbn of the book to delete
     * @return the deleted book
     */
    Book deleteBook(String isbn);

    /**
     * Updates a book in the service.
     * @param isbn the isbn of the book to update
     * @param book the book object with the new book informations
     * @return the updated book
     */
    Book updateBook(String isbn, Book book);

    /**
     * Marks a book as lended. After this the lender of the book is set.
     * @param userid the id of the user who lends the book
     * @param isbn the isbn of the lended book
     */
    void lendBook(int userid, String isbn);

    /**
     * Unmarks a lended book. After this the book is available and the lender of the book is null.
     * @param userid the id of the user who returns the book
     * @param isbn the isbn of the returned book
     */
    void returnBook(int userid, String isbn);

}
