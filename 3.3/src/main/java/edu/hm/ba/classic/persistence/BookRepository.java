package edu.hm.ba.classic.persistence;

import edu.hm.ba.classic.entities.Book;
import edu.hm.ba.classic.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Repository for the book.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    /**
     * Returns a list of books which are lent by the specified user.
     * @param user the lender
     * @return list of books with the specified lender
     */
    Collection<Book> findBooksByLender(User user);

}
