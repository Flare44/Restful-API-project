package de.hhu.cs.dbs.propra.domain.model;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByName(String name);

    long countByNameAndPassword(String name, String password);
}
