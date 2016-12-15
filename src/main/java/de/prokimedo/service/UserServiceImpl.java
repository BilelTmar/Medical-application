package de.prokimedo.service;
import com.google.common.collect.Lists;
import de.prokimedo.QueryService;
import de.prokimedo.entity.ProkimedoUser;
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
    public ProkimedoUser read(String firstNames) {
        List<ProkimedoUser> list = repo.findByFirstNames(firstNames);
        if(list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Save user
     */
    @Override
    public ProkimedoUser save(ProkimedoUser user) {
        this.repo.save(user);
        return user;
    }
}
