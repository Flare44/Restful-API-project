package de.hhu.cs.dbs.propra.application.services;

import de.hhu.cs.dbs.propra.domain.model.Role;
import de.hhu.cs.dbs.propra.domain.model.User;
import de.hhu.cs.dbs.propra.domain.model.UserRepository;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

public class CustomAuthorizationService implements AuthorizationService {
    @Inject
    private UserRepository userRepository;

    @Override
    public boolean authorise(String name, Set<Role> rolesAllowed) {
        Optional<User> user = userRepository.findByName(name);
        if (user.isEmpty()) return false;
        return user.get().getRoles().stream().anyMatch(rolesAllowed::contains);
    }
}
