package org.timeflies.projects;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


@Entity
@RegisterForReflection
public class Project extends PanacheEntity {

    @NotNull
    @Size(min = 2, max = 50)
    public String name;
    @NotNull
    public Long userId;
    @Size(max = 250)
    public String description;
    @Size(min = 2, max = 20)
    public String color = "#808080";
    public boolean isArchived;
    @NotNull
    public LocalDateTime lastModified;

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isArchived='" + isArchived + '\'' +
                ", lastModified=" + lastModified.toString() +
                '}';
    }

}
