package org.timeflies.users

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheEntityBase

import java.util.*

import javax.persistence.Column
import javax.persistence.Entity
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
open class Users : PanacheEntity() {
    @NotNull
    @Size(min = 3, max = 50)
    lateinit var userName: String

    @NotNull
    @Size(min = 3, max = 50)
    lateinit var lastName: String

    @NotNull
    @Size(min = 3, max = 50)
    lateinit var firstName: String
    var pictureUrl: String? = null

    @Column(columnDefinition = "TEXT")
    var status: String? = null

    fun findRandom(): Users {
        val count: Long = PanacheEntityBase.count()
        val random = Random()
        val randomUser: Int = random.nextInt(count.toInt())
        return findAll<Users>().page<Users>(randomUser, 1).firstResult()
    }

    override fun toString(): String {
        return "EndUser{" +
                "id='$id'" +
                ", userName='$userName'" +
                ", lastName='$lastName'" +
                ", firstName='$firstName'" +
                ", pictureUrl='$pictureUrl'" +
                ", status='$status'" +
                '}'
    }
}