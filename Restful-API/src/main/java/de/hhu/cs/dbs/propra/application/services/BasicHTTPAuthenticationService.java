package de.hhu.cs.dbs.propra.application.services;

import java.util.Base64;

import static javax.ws.rs.core.SecurityContext.BASIC_AUTH;

public class BasicHTTPAuthenticationService extends HTTPAuthenticationService {
    public final boolean authenticate(String base64EncodedCredentials) {
        String name = getNameFromEncodedCredentials(base64EncodedCredentials);
        String password = getPasswordFromEncodedCredentials(base64EncodedCredentials);
        return authenticate(name, password);
    }

    public String getNameFromEncodedCredentials(String base64EncodedCredentials) {
        String base64DecodedCredentials = decodeCredentials(base64EncodedCredentials);
        return getNameFromDecodedCredentials(base64DecodedCredentials);
    }

    public String getNameFromDecodedCredentials(String base64DecodedCredentials) {
        return base64DecodedCredentials.replaceFirst(":.*", "");
    }

    public String getPasswordFromEncodedCredentials(String base64EncodedCredentials) {
        String base64DecodedCredentials = decodeCredentials(base64EncodedCredentials);
        return getPasswordFromDecodedCredentials(base64DecodedCredentials);
    }

    public String getPasswordFromDecodedCredentials(String base64DecodedCredentials) {
        return base64DecodedCredentials.replaceFirst(".*?:", "");
    }

    public String encodeCredentials(String base64DecodedCredentials) {
        return Base64.getEncoder().encodeToString(base64DecodedCredentials.getBytes());
    }

    public String decodeCredentials(String base64EncodedCredentials) {
        return new String(Base64.getDecoder().decode(base64EncodedCredentials.getBytes()));
    }

    @Override
    public boolean authenticate(String name, String password) {
        return userRepository.countByNameAndPassword(name, password) == 1;
    }

    @Override
    public final boolean validateHeader(String header) {
        return header != null && header.startsWith("Basic ");
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    final public String getAuthenticationScheme() {
        return BASIC_AUTH;
    }
}
