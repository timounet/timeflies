package org.timeflies.timers.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.timeflies.timers.TimerResource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Liveness
@ApplicationScoped
public class PingTimerResourceHealthCheck implements HealthCheck {
    @Inject
    TimerResource resource;

    @Override
    public HealthCheckResponse call() {
        resource.hello();
        return HealthCheckResponse.named("Ping Timers REST Endpoint").up().build();
    }
}
