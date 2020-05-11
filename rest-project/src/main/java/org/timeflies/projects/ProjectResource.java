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

@Tag(name = "projects")
@Path("/api/projects")
@Produces(APPLICATION_JSON)
public class ProjectResource {
    private static final Logger log = Logger.getLogger(ProjectResource.class);

    @ConfigProperty(name = "projects.default.sort", defaultValue = "lastModified,name")
    public String sort;

    @Inject
    ProjectService service;

    @Operation(summary = "Returns a random project for a dedicated user")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Project.class, required = true)))
    @Counted(name = "countGetRandomProject", description = "Counts how many times the getRandom method has been invoked")
    @Timed(name = "timeGetRandomProject", description = "Times how long it takes to invoke the getRandom method", unit = MetricUnits.MILLISECONDS)
    @GET
    @Path("/user/{userId}/random/")
    public Response getRandom(
            @Parameter(description = "user Id")
            @QueryParam("userId") Long userId) {
        Project proj = service.userRandom(userId);
        log.debug("Found random proj " + proj);
        return Response.ok(proj).build();
    }

    @Operation(summary = "Returns all Projects for a user")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Project.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "204", description = "No Project")
    @Counted(name = "countGetAllUserProjects", description = "Counts how many times the getAllUserProjects Project method has been invoked")
    @Timed(name = "timeGetAllUserProjects", description = "Times how long it takes to invoke the getAllUserProjects method", unit = MetricUnits.MILLISECONDS)
    @GET
    @Path("/user/{userId}")
    public Response getAllUserProjects(
            @Parameter(description = "user Id")
            @QueryParam("userId") Long userId) {
        List<Project> Project = service.userList(userId);
        log.debug("Total number of Project " + Project.size() + " for user " + userId);
        return Response.ok(Project).build();
    }

    @Operation(summary = "Returns latest project for a given userId")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Project.class)))
    @APIResponse(responseCode = "204", description = "No project is not found for a given user identifier")
    @Counted(name = "countGetUserLatestProject", description = "Counts how many times the get getUserLatest by user id method has been invoked")
    @Timed(name = "timeGetUserLatestProject", description = "Times how long it takes to invoke the get getUserLatest by user id method", unit = MetricUnits.MILLISECONDS)
    @GET
    @Path("/user/{userId}/latest")
    public Response getUserLatest(
            @Parameter(description = "user Id")
            @QueryParam("userId") Long userId) {
        Project proj = service.userLatest(userId);
        if (proj != null) {
            log.debug("Found project " + proj);
            return Response.ok(proj).build();
        } else {
            log.debug("No project found with user id " + userId);
            return Response.noContent().build();
        }
    }

    @Operation(summary = "Returns all Projects from the database")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Project.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "204", description = "No Project")
    @Counted(name = "countGetAllProject", description = "Counts how many times the getAll Project method has been invoked")
    @Timed(name = "timeGetAllProject", description = "Times how long it takes to invoke the getAll method", unit = MetricUnits.MILLISECONDS)
    @GET
    public Response getAll() {
        List<Project> Project = service.findAll();
        log.debug("Total number of Project " + Project.size());
        return Response.ok(Project).build();
    }

    @Operation(summary = "Returns sorted Project from database")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Project.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "204", description = "No Project")
    @Counted(name = "countGetSortedProject", description = "Counts how many times the getSorted Project method has been invoked")
    @Timed(name = "timeGetSortedProject", description = "Times how long it takes to invoke the getSorted method", unit = MetricUnits.MILLISECONDS)
    @GET
    @Path("/sort")
    public Response sortedBy(
            @Parameter(description = "fields Comma list (sorted by default configuration Project.default.sort config properties)")
            @QueryParam("orderBy") String orderBy) {
        if (null == orderBy) orderBy = sort;
        List<Project> Project = service.listSortBy("order by " + orderBy);
        return Response.ok(Project).build();
    }

    @Operation(summary = "Returns a project for a given identifier")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Project.class)))
    @APIResponse(responseCode = "204", description = "The project is not found for a given identifier")
    @Counted(name = "countGetProject", description = "Counts how many times the get project by id method has been invoked")
    @Timed(name = "timeGetProject", description = "Times how long it takes to invoke the get project by id method", unit = MetricUnits.MILLISECONDS)
    @GET
    @Path("/{id}")
    public Response get(
            @Parameter(description = "project identifier", required = true)
            @PathParam("id") Long id) {
        Project proj = service.findById(id);
        if (proj != null) {
            log.debug("Found project " + proj);
            return Response.ok(proj).build();
        } else {
            log.debug("No project found with id " + id);
            return Response.noContent().build();
        }
    }

    @Operation(summary = "Creates a valid project")
    @APIResponse(responseCode = "201", description = "The URI of the created project", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = URI.class)))
    @Counted(name = "countCreateProject", description = "Counts how many times the project creation method has been invoked")
    @Timed(name = "timeCreateProject", description = "Times how long it takes to invoke the project creation method", unit = MetricUnits.MILLISECONDS)
    @POST
    @Consumes(APPLICATION_JSON)
    public Response create(
            @RequestBody(required = true, content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Project.class)))
            @Valid Project proj, @Context UriInfo uriInfo) {
        proj = service.persist(proj);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(proj.id));
        log.debug("New proj created with URI " + builder.build().toString());
        return Response.created(builder.build()).build();
    }

    @Operation(summary = "Updates an exiting  project")
    @APIResponse(responseCode = "200", description = "The updated project", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Project.class)))
    @Counted(name = "countUpdateProject", description = "Counts how many times the project update method has been invoked")
    @Timed(name = "timeUpdateProject", description = "Times how long it takes to invoke the project update method", unit = MetricUnits.MILLISECONDS)
    @PUT
    @Consumes(APPLICATION_JSON)
    public Response update(
            @RequestBody(required = true, content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Project.class)))
            @Valid Project proj) {
        proj = service.update(proj);
        log.debug("proj updated with new valued " + proj);
        return Response.ok(proj).build();
    }

    @Operation(summary = "Deletes an exiting project")
    @APIResponse(responseCode = "204")
    @Counted(name = "countDeleteProject", description = "Counts how many times the project deletion method has been invoked")
    @Timed(name = "timeDeleteProject", description = "Times how long it takes to invoke the project deletion method", unit = MetricUnits.MILLISECONDS)
    @DELETE
    @Path("/{id}")
    public Response delete(
            @Parameter(description = "project identifier", required = true)
            @PathParam("id") Long id) {
        service.delete(id);
        log.debug("Project deleted with " + id);
        return Response.noContent().build();
    }

    @Operation(summary = "Display a hello message with the class simple name who answers")
    @APIResponse(responseCode = "200", description = "A hello message and a class name", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    @Counted(name = "countHelloProject", description = "Counts how many times the ProjectResource hello method has been invoked")
    @Timed(name = "timeHelloProject", description = "Times how long it takes to invoke the ProjectResource hello method", unit = MetricUnits.MILLISECONDS)
    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello " + ProjectResource.class.getSimpleName();
    }
}