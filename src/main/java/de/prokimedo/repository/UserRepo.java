package de.prokimedo.repository;
import de.prokimedo.entity.ProkimedoUser;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<ProkimedoUser, Long> {

    List<ProkimedoUser> findByFirstNames(String firstNames);

}
