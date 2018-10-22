package edu.hm.ba.klassisch.persistence;

import edu.hm.ba.klassisch.entities.Lending;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource(path = "lending")
public interface LendingRepository extends CrudRepository<Lending, Integer>{
}
