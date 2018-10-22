package edu.hm.ba.klassisch.persistence;

import edu.hm.ba.klassisch.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource(path = "user")
public interface UserRepository extends CrudRepository<User, Integer> {

}
