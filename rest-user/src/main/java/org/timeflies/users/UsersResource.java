package org.timeflies.users;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {
    private static final Logger log = Logger.getLogger(UsersResource.class);
    @ConfigProperty(name = "users.default.sort", defaultValue = "lastName,firstName,userName")
    public String sort;
    @Inject
    UsersService service;

    @GET
    @Path("/random")
    public Response getRandom() {
        Users user = service.findRandom();
        log.debug("Found random user " + user);
        return Response.ok(user).build();
    }

    @GET
    public Response getAll() {
        List<Users> users = service.findAll();
        log.debug("Total number of users " + users.size());
        return Response.ok(users).build();
    }

    @GET
    @Path("/sort")
    public Response sorted() {
        return sortedBy(sort);
    }

    @GET
    @Path("/sort")
    public Response sortedBy(@QueryParam("orderBy") String orderBy) {
        List<Users> users = service.listSortBy("order by " + orderBy);
        return Response.ok(users).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        Users user = service.findById(id);
        if (user != null) {
            log.debug("Found user " + user);
            return Response.ok(user).build();
        } else {
            log.debug("No user found with id " + id);
            return Response.noContent().build();
        }
    }

    @POST
    public Response create(@Valid Users user, @Context UriInfo uriInfo) {
        user = service.persist(user);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(user.id));
        log.debug("New user created with URI " + builder.build().toString());
        return Response.created(builder.build()).build();
    }

    @PUT
    public Response update(@Valid Users user) {
        user = service.update(user);
        log.debug("User updated with new valued " + user);
        return Response.ok(user).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        log.debug("Users deleted with " + id);
        return Response.noContent().build();
    }

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }
}