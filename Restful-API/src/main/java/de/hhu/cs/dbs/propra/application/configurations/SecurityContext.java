package de.hhu.cs.dbs.propra.application.configurations;

import de.hhu.cs.dbs.propra.domain.model.Role;
import de.hhu.cs.dbs.propra.domain.model.User;

import java.security.Principal;

public class SecurityContext implements javax.ws.rs.core.SecurityContext {
    private User user;

    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    @Override
    public boolean isUserInRole(String role) {
        return user != null && user.getRoles().contains(Role.valueOf(role));
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        return BASIC_AUTH;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
