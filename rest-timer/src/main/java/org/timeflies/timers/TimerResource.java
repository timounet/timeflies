package org.timeflies.timers;

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
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/api/timers")
public class TimerResource {

    private static final Logger log = Logger.getLogger(TimerResource.class);

    @ConfigProperty(name = "timers.default.sort", defaultValue = "start")
    public String sort;

    @Inject
    TimerService service;


    @Operation(summary = "Returns all Timers for a user")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Timer.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "204", description = "No Timer")
    @Counted(name = "countGetAllUserTimers", description = "Counts how many times the getAllUserTimers Timer method has been invoked")
    @Timed(name = "timeGetAllUserTimers", description = "Times how long it takes to invoke the getAllUserTimers method", unit = MetricUnits.MILLISECONDS)
    @GET
    @Path("/user/{userId}")
    public Response getAllUserTimers(
            @Parameter(description = "user Id")
            @PathParam("userId") Long userId) {
        List<Timer> timers = service.userList(userId);
        log.debug("Total number of timers " + timers.size() + " for user " + userId);
        return Response.ok(timers).build();
    }

    @Operation(summary = "Returns latest Timer for a given userId")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Timer.class)))
    @APIResponse(responseCode = "204", description = "No Timer is not found for a given user identifier")
    @Counted(name = "countGetUserLatestTimer", description = "Counts how many times the get getUserLatest by user id method has been invoked")
    @Timed(name = "timeGetUserLatestTimer", description = "Times how long it takes to invoke the get getUserLatest by user id method", unit = MetricUnits.MILLISECONDS)
    @GET
    @Path("/user/{userId}/latest")
    public Response getUserLatest(
            @Parameter(description = "user Id")
            @PathParam("userId") Long userId) {
        Timer timer = service.userLatest(userId);
        if (timer != null) {
            log.debug("Found Timer " + timer);
            return Response.ok(timer).build();
        } else {
            log.debug("No Timer found with user id " + userId);
            return Response.noContent().build();
        }
    }

    @Operation(summary = "Returns all Timers for a project")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Timer.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "204", description = "No Timer")
    @Counted(name = "countGetAllProjectTimers", description = "Counts how many times the getAllProjectTimers Timer method has been invoked")
    @Timed(name = "timeGetAllProjectTimers", description = "Times how long it takes to invoke the getAllProjectTimers method", unit = MetricUnits.MILLISECONDS)
    @GET
    @Path("/project/{projectId}")
    public Response getAllProjectTimers(
            @Parameter(description = "project Id")
            @PathParam("projectId") Long projectId) {
        List<Timer> timers = service.projectList(projectId);
        log.debug("Total number of timers " + timers.size() + " for project " + projectId);
        return Response.ok(timers).build();
    }

    @Operation(summary = "Returns latest Timer for a given projectId")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Timer.class)))
    @APIResponse(responseCode = "204", description = "No Timer is not found for a given project identifier")
    @Counted(name = "countgetProjectLatestTimer", description = "Counts how many times the get getProjectLatest by project id method has been invoked")
    @Timed(name = "timeGetgetProjectLatestTimer", description = "Times how long it takes to invoke the get getProjectLatest by project id method", unit = MetricUnits.MILLISECONDS)
    @GET
    @Path("/project/{projectId}/latest")
    public Response getProjectLatest(
            @Parameter(description = "project id")
            @PathParam("projectId") Long projectId) {
        Timer timer = service.projectLatest(projectId);
        if (timer != null) {
            log.debug("Found Timer " + timer);
            return Response.ok(timer).build();
        } else {
            log.debug("No Timer found with project id " + projectId);
            return Response.noContent().build();
        }
    }

    @Operation(summary = "Returns all Timers from the database")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Timer.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "204", description = "No Timer")
    @Counted(name = "countGetAllTimer", description = "Counts how many times the getAll Timer method has been invoked")
    @Timed(name = "timeGetAllTimer", description = "Times how long it takes to invoke the getAll method", unit = MetricUnits.MILLISECONDS)
    @GET
    public Response getAll() {
        List<Timer> Timer = service.findAll();
        log.debug("Total number of Timer " + Timer.size());
        return Response.ok(Timer).build();
    }

    @Operation(summary = "Returns sorted Timer from database")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Timer.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "204", description = "No Timer")
    @Counted(name = "countGetSortedTimer", description = "Counts how many times the getSorted Timer method has been invoked")
    @Timed(name = "timeGetSortedTimer", description = "Times how long it takes to invoke the getSorted method", unit = MetricUnits.MILLISECONDS)
    @GET
    @Path("/sort")
    public Response sortedBy(
            @Parameter(description = "fields Comma list (sorted by default configuration Timer.default.sort config properties)")
            @QueryParam("orderBy") String orderBy) {
        if (null == orderBy) orderBy = sort;
        List<Timer> Timer = service.listSortBy("order by " + orderBy);
        return Response.ok(Timer).build();
    }

    @Operation(summary = "Returns a Timer for a given identifier")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Timer.class)))
    @APIResponse(responseCode = "204", description = "The Timer is not found for a given identifier")
    @Counted(name = "countGetTimer", description = "Counts how many times the get Timer by id method has been invoked")
    @Timed(name = "timeGetTimer", description = "Times how long it takes to invoke the get Timer by id method", unit = MetricUnits.MILLISECONDS)
    @GET
    @Path("/{id}")
    public Response get(
            @Parameter(description = "Timer identifier", required = true)
            @PathParam("id") Long id) {
        Timer timer = service.findById(id);
        if (timer != null) {
            log.debug("Found Timer " + timer);
            return Response.ok(timer).build();
        } else {
            log.debug("No Timer found with id " + id);
            return Response.noContent().build();
        }
    }

    @Operation(summary = "Creates a valid Timer")
    @APIResponse(responseCode = "201", description = "The URI of the created Timer", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = URI.class)))
    @Counted(name = "countCreateTimer", description = "Counts how many times the Timer creation method has been invoked")
    @Timed(name = "timeCreateTimer", description = "Times how long it takes to invoke the Timer creation method", unit = MetricUnits.MILLISECONDS)
    @POST
    @Consumes(APPLICATION_JSON)
    public Response create(
            @RequestBody(required = true, content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Timer.class)))
            @Valid Timer timer, @Context UriInfo uriInfo) {
        timer = service.persist(timer);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(timer.id));
        log.debug("New timer created with URI " + builder.build().toString());
        return Response.created(builder.build()).build();
    }

    @Operation(summary = "Updates an exiting  Timer")
    @APIResponse(responseCode = "200", description = "The updated Timer", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Timer.class)))
    @Counted(name = "countUpdateTimer", description = "Counts how many times the Timer update method has been invoked")
    @Timed(name = "timeUpdateTimer", description = "Times how long it takes to invoke the Timer update method", unit = MetricUnits.MILLISECONDS)
    @PUT
    @Consumes(APPLICATION_JSON)
    public Response update(
            @RequestBody(required = true, content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Timer.class)))
            @Valid Timer timer) {
        timer = service.update(timer);
        log.debug("timer updated with new valued " + timer);
        return Response.ok(timer).build();
    }

    @Operation(summary = "Deletes an exiting Timer")
    @APIResponse(responseCode = "204")
    @Counted(name = "countDeleteTimer", description = "Counts how many times the Timer deletion method has been invoked")
    @Timed(name = "timeDeleteTimer", description = "Times how long it takes to invoke the Timer deletion method", unit = MetricUnits.MILLISECONDS)
    @DELETE
    @Path("/{id}")
    public Response delete(
            @Parameter(description = "Timer identifier", required = true)
            @PathParam("id") Long id) {
        service.delete(id);
        log.debug("Timer deleted with " + id);
        return Response.noContent().build();
    }


    @Operation(summary = "Display a hello message with the class simple name who answers")
    @APIResponse(responseCode = "200", description = "A hello message and a class name", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    @Counted(name = "countHelloTimer", description = "Counts how many times the TimerResource hello method has been invoked")
    @Timed(name = "timeHelloTimer", description = "Times how long it takes to invoke the TimerResource hello method", unit = MetricUnits.MILLISECONDS)
    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello " + TimerResource.class.getSimpleName();
    }
}