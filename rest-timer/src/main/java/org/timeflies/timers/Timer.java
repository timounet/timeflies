package org.timeflies.timers;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@RegisterForReflection
public class Timer extends PanacheEntity {
    public LocalDateTime start = LocalDateTime.now();
    public LocalDateTime end;
    @NotNull
    public Long userId;
    @NotNull
    public Long projectId;

    @Override
    public String toString() {
        return "Timer{" +
                "id=" + id +
                ", projectId='" + projectId + '\'' +
                ", userId='" + userId + '\'' +
                ", start=" + start.toString() +
                ", end=" + end.toString() +
                '}';
    }
}
