package org.timeflies.users;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.security.SecureRandom;

@Entity
public class Users extends PanacheEntity {
    @NotNull
    @Size(min = 3, max = 50)
    public String userName;
    @NotNull
    @Size(min = 2, max = 50)
    public String lastName;
    @NotNull
    @Size(min = 2, max = 50)
    public String firstName;
    public String pictureUrl;
    @Column(columnDefinition = "TEXT")
    public String status;

    public static Users findRandom() {
        long count = Users.count();
        SecureRandom random = new SecureRandom();
        int randomUser = random.nextInt((int) count);
        return Users.findAll().page(randomUser, 1).firstResult();
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName=" + firstName +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
