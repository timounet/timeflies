package org.timeflies.security.authorization;

import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.jwt.auth.principal.JWTCallerPrincipal;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api/users")
public class UsersResource {

    @Inject
    SecurityIdentity identity;

    @GET
    @Path("/me")
    @Produces(MediaType.APPLICATION_JSON)
    @NoCache
    public User me() {
        return new User(identity);
    }

    public static class User {

        private final String userName;
        private final String name;
        private final String givenName;
        private final String familyName;
        private final String email;

        User(SecurityIdentity identity) {
            this.userName = identity.getPrincipal().getName();
            JWTCallerPrincipal u = (JWTCallerPrincipal) identity.getPrincipal();
            this.name = u.getClaim("name");
            this.email = u.getClaim("email");
            this.givenName = u.getClaim("given_name");
            this.familyName = u.getClaim("family_name");
        }

        public String getUserName() {
            return userName;
        }

        public String getName() {
            return name;
        }

        public String getGivenName() {
            return givenName;
        }

        public String getFamilyName() {
            return familyName;
        }

        public String getEmail() {
            return email;
        }

    }
}
