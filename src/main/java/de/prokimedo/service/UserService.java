package de.prokimedo.service;

import de.prokimedo.entity.User;
import java.util.List;

public interface UserService {

    public User read(String firstNames);

    public User save(User user);

}
