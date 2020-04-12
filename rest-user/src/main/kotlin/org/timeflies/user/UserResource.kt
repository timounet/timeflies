package org.timeflies.user

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/api/users")
class UserResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun hello() = "hello"
}