package edu.hm.ba.classic.services;

import edu.hm.ba.classic.entities.Book;
import edu.hm.ba.classic.entities.User;
import edu.hm.ba.classic.persistence.BookRepository;
import edu.hm.ba.classic.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

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
}
