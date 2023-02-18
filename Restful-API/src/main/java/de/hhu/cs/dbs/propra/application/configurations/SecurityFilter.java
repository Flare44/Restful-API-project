package de.hhu.cs.dbs.propra.application.configurations;

import de.hhu.cs.dbs.propra.application.services.BasicHTTPAuthenticationService;
import de.hhu.cs.dbs.propra.application.services.CustomAuthorizationService;
import de.hhu.cs.dbs.propra.domain.model.Role;
import de.hhu.cs.dbs.propra.domain.model.UserRepository;
import org.glassfish.jersey.server.model.AnnotatedMethod;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {
    @Context
    private ResourceInfo resourceInfo;
    @Inject
    private UserRepository userRepository;
    @Inject
    private BasicHTTPAuthenticationService authenticationService;
    @Inject
    private CustomAuthorizationService authorizationService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        SecurityContext securityContext = new SecurityContext();
        requestContext.setSecurityContext(securityContext);

        final AnnotatedMethod annotatedMethod = new AnnotatedMethod(resourceInfo.getResourceMethod());
        if (annotatedMethod.isAnnotationPresent(DenyAll.class)) throw new ForbiddenException("Forbidden");
        if (!annotatedMethod.isAnnotationPresent(RolesAllowed.class)) return;

        RolesAllowed annotation = annotatedMethod.getAnnotation(RolesAllowed.class);
        Set<Role> rolesAllowed = getRolesAllowedFromAnnotation(annotation);

        final String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (!authenticationService.validateHeader(authorizationHeader))
            throw new NotAuthorizedException(authenticationService.getWWWAuthenticateHeader());
        String base64EncodedCredentials = authorizationHeader.substring("Basic ".length());

        final String name = authenticationService.getNameFromEncodedCredentials(base64EncodedCredentials);
        final String password = authenticationService.getPasswordFromEncodedCredentials(base64EncodedCredentials);
        if (!authenticationService.authenticate(name, password))
            throw new NotAuthorizedException(authenticationService.getWWWAuthenticateHeader());

        securityContext.setUser(userRepository.findByName(name).orElse(null));

        if (!authorizationService.authorise(name, rolesAllowed)) throw new ForbiddenException("Forbidden");
    }

    private Set<Role> getRolesAllowedFromAnnotation(RolesAllowed annotation) {
        Set<Role> rolesAllowed = new HashSet<>();
        for (String value : annotation.value()) {
            rolesAllowed.add(Role.valueOf(value));
        }
        return rolesAllowed;
    }
}
