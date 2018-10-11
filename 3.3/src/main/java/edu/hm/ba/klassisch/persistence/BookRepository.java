package edu.hm.ba.klassisch.persistence;

import edu.hm.ba.klassisch.entities.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "book")
public interface BookRepository extends CrudRepository<Book, String>{
}
