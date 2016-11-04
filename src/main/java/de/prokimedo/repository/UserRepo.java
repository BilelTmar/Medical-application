package de.prokimedo.repository;
import de.prokimedo.entity.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Long> {

    List<User> findByFirstNames(String firstNames);

}
