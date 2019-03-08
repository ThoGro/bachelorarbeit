package edu.hm.ba.classic.services;

import edu.hm.ba.classic.entities.Book;
import edu.hm.ba.classic.entities.User;
import edu.hm.ba.classic.exception.BookDoesNotExistException;
import edu.hm.ba.classic.exception.CouldNotCreateBookException;
import edu.hm.ba.classic.exception.CouldNotLendBookException;
import edu.hm.ba.classic.exception.DuplicateBookException;
import edu.hm.ba.classic.persistence.BookRepository;
import edu.hm.ba.classic.persistence.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Implementation of the BookService.
 * @author Thomas Großbeck
 */
@Service
@Transactional(rollbackOn = { Exception.class })
public class BookServiceImpl implements BookService {

    /**
     * Length of an isbn code.
     */
    public static final int ISBN_LENGTH = 13;

    /**
     * Book Repository.
     */
    private BookRepository bookRepository;

    /**
     * User Repository.
     */
    private UserRepository userRepository;

    /**
     * Constructs a service instance with the two needed repositories.
     * @param bookRepository the book repository
     * @param userRepository the user repository
     */
    public BookServiceImpl(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Collection<Book> getBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book addBook(Book book) {
        if (!checkIsbn(book.getIsbn()) || book.getTitle().isEmpty() || book.getAuthor().isEmpty()) {
            throw new CouldNotCreateBookException("Unable to add book with isbn " + book.getIsbn() +
                    " title " + book.getTitle() + " and author " + book.getAuthor());
        }
        book.setIsbn(book.getIsbn().replace("-", "")); //mögliche Trennzeichen entfernen
        //Auf Duplikat prüfen
        for (Book b : getBooks()) {
            if (b.getIsbn().equals(book.getIsbn())) {
                throw new DuplicateBookException("The book with isbn " + book.getIsbn() + " does already exist.");
            }
        }
        return bookRepository.save(book);
    }

    @Override
    public Book deleteBook(String isbn) {
        Optional<Book> toDelete = bookRepository.findById(isbn);
        if (toDelete == null) {
            throw new BookDoesNotExistException("The book with isbn " + isbn + " can not be found.");
        }
        bookRepository.deleteById(isbn);
        return toDelete.orElse(null);
    }

    @Override
    public Book updateBook(String isbn, Book book) {
        Book toUpdate = bookRepository.getOne(isbn);
        if (!isbn.equals(book.getIsbn()) || book.getTitle().isEmpty() || book.getAuthor().isEmpty()) {
            throw new CouldNotCreateBookException("Unable to update book with isbn " + book.getIsbn() +
                    " title " + book.getTitle() + " and author " + book.getAuthor());
        }
        toUpdate.setTitle(book.getTitle());
        toUpdate.setAuthor(book.getAuthor());
        toUpdate.setCategory(book.getCategory());
        return bookRepository.save(toUpdate);
    }

    @Override
    public User lendBook(String isbn, Authentication authentication) {
        if (bookRepository.existsById(isbn)) {
            Book toLend = bookRepository.getOne(isbn);
            User lender = userRepository.findUserByUsername(authentication.getName());
            toLend.setLender(lender);
            bookRepository.save(toLend);
            return lender;
        } else {
            throw new CouldNotLendBookException("Book " + isbn + " could not be lent from user " + authentication.getName());
        }
    }

    @Override
    public User returnBook(String isbn, Authentication authentication) {
        if (bookRepository.existsById(isbn)) {
            Book toReturn = bookRepository.getOne(isbn);
            User returner = userRepository.findUserByUsername(authentication.getName());
            if (toReturn.getLender() != null || !toReturn.getLender().equals(returner)) {
                toReturn.setLender(null);
                bookRepository.save(toReturn);
                return returner;
            } else {
                throw new CouldNotLendBookException("Book " + isbn + " could not be returned from user " + authentication.getName());
            }
        } else {
            throw new CouldNotLendBookException("Book " + isbn + " could not be returned from user " + authentication.getName());
        }
    }

    @Override
    public Collection<Book> getLendings(Authentication authentication) {
        User lender = userRepository.findUserByUsername(authentication.getName());
        return lender.getLendings();
    }

    /**
     * Checks if there is a valid isbn number.
     * @param isbn isbn to check
     * @return true if the isbn is valid
     */
    private boolean checkIsbn(String isbn) {
        if (isbn.isEmpty()) {
            return false;
        } else {
            isbn = isbn.replace("-", ""); //mögliche Trennzeichen entfernen
        }
        if (isbn.length() != ISBN_LENGTH) {
            return false;
        }
        ArrayList<Integer> ints = convertToList(isbn);
        int checksum = (ints.get(0) + ints.get(2) + ints.get(4) + ints.get(6) + ints.get(8) + ints.get(10) + ints.get(12) + 3 * (ints.get(1) + ints.get(3) + ints.get(5) + ints.get(7) + ints.get(9) + ints.get(11))) % 10;
        if (checksum == 0) {
            return true;
        }
        return false;
    }

    /**
     * Converts a String of numbers to a list of integers.
     * @param isbn String to convert
     * @return list of integers
     */
    private ArrayList<Integer> convertToList(String isbn) {
        ArrayList<Integer> result = new ArrayList<>();
        for (char c: isbn.toCharArray()) {
            result.add(Character.getNumericValue(c));
        }
        return result;
    }
}
