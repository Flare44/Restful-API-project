package de.hhu.cs.dbs.propra.application.services;

import de.hhu.cs.dbs.propra.domain.model.Role;

import java.util.Set;

public interface AuthorizationService {
    boolean authorise(String name, Set<Role> rolesAllowed);
}
