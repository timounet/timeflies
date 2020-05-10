package org.timeflies.projects;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Tag(name = "users")
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
    @Counted(name = "countGetRandomUser", description = "Counts how many times the getRandom method has been invoked")
    @Timed(name = "timeGetRandomUser", description = "Times how long it takes to invoke the getRandom method", unit = MetricUnits.MILLISECONDS)
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
    @Counted(name = "countGetAllUsers", description = "Counts how many times the getAll users method has been invoked")
    @Timed(name = "timeGetAllUsers", description = "Times how long it takes to invoke the getAll method", unit = MetricUnits.MILLISECONDS)
    @GET
    public Response getAll() {
        List<Users> users = service.findAll();
        log.debug("Total number of users " + users.size());
        return Response.ok(users).build();
    }

    @Operation(summary = "Returns sorted users from database")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Users.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "204", description = "No users")
    @Counted(name = "countGetSortedUsers", description = "Counts how many times the getSorted users method has been invoked")
    @Timed(name = "timeGetSortedUsers", description = "Times how long it takes to invoke the getSorted method", unit = MetricUnits.MILLISECONDS)
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
    @Counted(name = "countGetUser", description = "Counts how many times the get user by id method has been invoked")
    @Timed(name = "timeGetUser", description = "Times how long it takes to invoke the get user by id method", unit = MetricUnits.MILLISECONDS)
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
    @Counted(name = "countCreateUser", description = "Counts how many times the user creation method has been invoked")
    @Timed(name = "timeCreateUser", description = "Times how long it takes to invoke the user creation method", unit = MetricUnits.MILLISECONDS)
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
    @Counted(name = "countUpdateUser", description = "Counts how many times the user update method has been invoked")
    @Timed(name = "timeUpdateUser", description = "Times how long it takes to invoke the user update method", unit = MetricUnits.MILLISECONDS)
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
    @Counted(name = "countDeleteUser", description = "Counts how many times the user deletion method has been invoked")
    @Timed(name = "timeDeleteUser", description = "Times how long it takes to invoke the user deletion method", unit = MetricUnits.MILLISECONDS)
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
    @Counted(name = "countHelloUsers", description = "Counts how many times the UsersResource hello method has been invoked")
    @Timed(name = "timeHelloUsers", description = "Times how long it takes to invoke the UsersResource hello method", unit = MetricUnits.MILLISECONDS)
    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello " + UsersResource.class.getSimpleName();
    }
}