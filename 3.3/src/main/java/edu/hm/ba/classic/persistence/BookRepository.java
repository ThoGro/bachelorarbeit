package edu.hm.ba.classic.persistence;

import edu.hm.ba.classic.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the book.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, String> {
}
