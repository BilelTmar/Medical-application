package de.prokimedo.service;

import de.prokimedo.entity.ProkimedoUser;
import java.util.List;

public interface UserService {

    public ProkimedoUser read(String firstNames);

    public ProkimedoUser save(ProkimedoUser user);

}
