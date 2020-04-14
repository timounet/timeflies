package org.timeflies.users;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/api/users")
@Produces(APPLICATION_JSON)
public class UsersResource {
    private static final Logger log = Logger.getLogger(UsersResource.class);
    @ConfigProperty(name = "users.default.sort", defaultValue = "lastName,firstName,userName")
    public String sort;
    @Inject
    UsersService service;

    @Operation(summary = "Returns a random user")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Users.class, required = true)))
    @GET
    @Path("/random")
    public Response getRandom() {
        Users user = service.findRandom();
        log.debug("Found random user " + user);
        return Response.ok(user).build();
    }

    @Operation(summary = "Returns all the users from the database")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Users.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "204", description = "No users")
    @GET
    public Response getAll() {
        List<Users> users = service.findAll();
        log.debug("Total number of users " + users.size());
        return Response.ok(users).build();
    }

    @Operation(summary = "Returns sorted users from database")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Users.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "204", description = "No users")
    @GET
    @Path("/sort")
    public Response sortedBy(
            @Parameter(description = "fields Comma list (sorted by default configuration users.default.sort config properties)")
            @QueryParam("orderBy") String orderBy) {
        if (null == orderBy) orderBy = sort;
        List<Users> users = service.listSortBy("order by " + orderBy);
        return Response.ok(users).build();
    }

    @Operation(summary = "Returns a user for a given identifier")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Users.class)))
    @APIResponse(responseCode = "204", description = "The user is not found for a given identifier")
    @GET
    @Path("/{id}")
    public Response get(
            @Parameter(description = "User identifier", required = true)
            @PathParam("id") Long id) {
        Users user = service.findById(id);
        if (user != null) {
            log.debug("Found user " + user);
            return Response.ok(user).build();
        } else {
            log.debug("No user found with id " + id);
            return Response.noContent().build();
        }
    }

    @Operation(summary = "Creates a valid User")
    @APIResponse(responseCode = "201", description = "The URI of the created User", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = URI.class)))
    @POST
    @Consumes(APPLICATION_JSON)
    public Response create(
            @RequestBody(required = true, content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Users.class)))
            @Valid Users user, @Context UriInfo uriInfo) {
        user = service.persist(user);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(user.id));
        log.debug("New user created with URI " + builder.build().toString());
        return Response.created(builder.build()).build();
    }

    @Operation(summary = "Updates an exiting  user")
    @APIResponse(responseCode = "200", description = "The updated user", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Users.class)))
    @PUT
    @Consumes(APPLICATION_JSON)
    public Response update(
            @RequestBody(required = true, content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Users.class)))
            @Valid Users user) {
        user = service.update(user);
        log.debug("User updated with new valued " + user);
        return Response.ok(user).build();
    }

    @Operation(summary = "Deletes an exiting user")
    @APIResponse(responseCode = "204")
    @DELETE
    @Path("/{id}")
    public Response delete(
            @Parameter(description = "User identifier", required = true)
            @PathParam("id") Long id) {
        service.delete(id);
        log.debug("Users deleted with " + id);
        return Response.noContent().build();
    }

    @Operation(summary = "Display a hello message with the class simple name who answers")
    @APIResponse(responseCode = "200", description = "A hello message and a class name", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello " + UsersResource.class.getSimpleName();
    }
}