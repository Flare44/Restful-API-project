package de.hhu.cs.dbs.propra.application.services;

public interface AuthenticationService {
    boolean authenticate(String name, String password);

    String getAuthenticationScheme();
}
