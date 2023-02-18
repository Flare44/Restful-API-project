package de.hhu.cs.dbs.propra.domain.model;

import java.security.Principal;
import java.util.Collections;
import java.util.Set;

public class User implements Principal {
    private final String name;
    private final Set<Role> roles;

    public User(String name) {
        this(name, Collections.emptySet());
    }

    public User(String name, Set<Role> roles) {
        this.name = name;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", roles=" + roles +
                '}';
    }
}
