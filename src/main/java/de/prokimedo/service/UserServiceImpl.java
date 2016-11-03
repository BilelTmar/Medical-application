package de.prokimedo.service;
import com.google.common.collect.Lists;
import de.prokimedo.QueryService;
import de.prokimedo.entity.User;
import de.prokimedo.repository.UserRepo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    UserRepo repo;

    //! Copy constructor.
    @Autowired
    public UserServiceImpl(UserRepo repo) {
        this.repo = repo;
    }


    @Override
    public User read(String firstName) {

        List<User> list = repo.findByFirstName(firstName);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    /**
     * Save user
     */
    @Override
    public User save(User user) {
        this.repo.save(user);
        return user;
    }
}
