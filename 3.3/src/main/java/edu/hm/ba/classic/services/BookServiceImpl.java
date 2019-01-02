package edu.hm.ba.classic.services;

import edu.hm.ba.classic.entities.Book;
import edu.hm.ba.classic.entities.User;
import edu.hm.ba.classic.persistence.BookRepository;
import edu.hm.ba.classic.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Implementation of the BookService.
 * @author Thomas Großbeck
 */
@Service
public class BookServiceImpl implements BookService {

    /**
     * Book Repository.
     */
    @Autowired
    BookRepository bookRepository;

    /**
     * User Repository.
     */
    @Autowired
    UserRepository userRepository;

    @Override
    public Collection<Book> getBooks() {
        return bookRepository.findAll();
    }

    @Override
    public void addBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public void deleteBook(String isbn) {
        bookRepository.deleteById(isbn);
    }

    @Override
    public void updateBook(Book book) {
        Book toUpdate = bookRepository.getOne(book.getIsbn());
        toUpdate.setTitle(book.getTitle());
        toUpdate.setAuthor(book.getAuthor());
        toUpdate.setCategory(book.getCategory());
        bookRepository.save(toUpdate);
    }

    @Override
    public void lendBook(int userid, String isbn) {
        if (userRepository.existsById(userid) && bookRepository.existsById(isbn)) {
            Book toLend = bookRepository.getOne(isbn);
            User lender = userRepository.getOne(userid);
            toLend.setLender(lender);
            bookRepository.save(toLend);
        }
    }

    @Override
    public void returnBook(int userid, String isbn) {
        if (userRepository.existsById(userid) && bookRepository.existsById(isbn)) {
            Book toReturn = bookRepository.getOne(isbn);
            User returner = userRepository.getOne(userid);
            if (toReturn.getLender() == null || toReturn.getLender().equals(returner)) {
                toReturn.setLender(null);
                bookRepository.save(toReturn);
            }
        }
    }
}
