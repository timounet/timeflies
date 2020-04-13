package org.timeflies.users

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/api/users")
class UsersResource {

    @Inject
    lateinit var service :UsersService

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    fun hello() = "hello"

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllUsers() : Response {
        val users = service.findAll()
        return Response.ok(users).build()
    }
}