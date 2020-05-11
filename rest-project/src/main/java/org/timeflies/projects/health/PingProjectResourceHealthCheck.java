package org.timeflies.projects.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.timeflies.projects.ProjectResource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Liveness
@ApplicationScoped
public class PingProjectResourceHealthCheck implements HealthCheck {
    @Inject
    ProjectResource resource;

    @Override
    public HealthCheckResponse call() {
        resource.hello();
        return HealthCheckResponse.named("Ping Users REST Endpoint").up().build();
    }
}
