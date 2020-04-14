package org.timeflies.users.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.timeflies.users.UsersResource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Liveness
@ApplicationScoped
public class PingUsersResourceHealthCheck implements HealthCheck {
    @Inject
    UsersResource resource;

    @Override
    public HealthCheckResponse call() {
        resource.hello();
        return HealthCheckResponse.named("Ping Users REST Endpoint").up().build();
    }
}
