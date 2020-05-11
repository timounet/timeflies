package org.timeflies.projects;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Project extends PanacheEntity {

    @NotNull
    @Size(min = 2, max = 50)
    public String name;
    @NotNull
    public Long userId;
    @Size(max = 250)
    public String description;
    @Size(min = 2, max = 20)
    public String colour = "#808080";
    public boolean isArchived = false;
    @NotNull
    public Date lastModified = new Date();

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isArchived='" + isArchived + '\'' +
                ", lastModified=" + new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(lastModified) +
                '}';
    }

}
