package org.timeflies.timers.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;
import org.timeflies.timers.Timer;
import org.timeflies.timers.TimerService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@Readiness
@ApplicationScoped
public class DatabaseConnectionHealthCheck implements HealthCheck {
    @Inject
    TimerService service;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse
                .named("Timers Datasource connection health check");
        try {
            List<Timer> projects = service.findAll();
            responseBuilder.withData("Number of projects in the database", projects.size()).up();
        } catch (IllegalStateException e) {
            responseBuilder.down();
        }
        return responseBuilder.build();
    }
}
