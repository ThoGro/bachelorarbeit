package edu.hm.ba.classic.services;

import edu.hm.ba.classic.entities.Book;

import java.util.Collection;

public interface BookService {

    Collection<Book> getBooks();

    void addBook(Book book);

    void deleteBook(String isbn);

    void updateBook(Book book);

    void lendBook(int userid, String isbn);

    void returnBook(int userid, String isbn);

}
