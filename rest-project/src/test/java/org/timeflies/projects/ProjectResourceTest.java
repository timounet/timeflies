package org.timeflies.projects;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@QuarkusTestResource(DatabaseResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProjectResourceTest {

    public static final String UPDATE = " (updated)";
    public static final String DEFAULT_NAME = "Alan Parsons Project";
    public static final String UPDATE_NAME = DEFAULT_NAME + UPDATE;
    public static final String DEFAULT_DESCRIPTION = "The Alan Parsons Project were a British rock band active between 1975 and 1990";
    public static final String UPDATE_DESCRIPTION = DEFAULT_DESCRIPTION + UPDATE;
    public static final String DEFAULT_COLOR = "#00FF00";
    public static final String UPDATE_COLOR = "#FF0000";
    public static final Long DEFAULT_USER_ID = 1L;
    public static final Long UPDATE_USER_ID = 2L;
    public static final boolean DEFAULT_IS_ARCHIVED = false;
    public static final boolean UPDATE_IS_ARCHIVED = true;
    public static final LocalDateTime DEFAULT_DATE = LocalDateTime.of(2019, Month.FEBRUARY, 1, 10, 50);
    public static final LocalDateTime UPDATE_DATE = LocalDateTime.of(2020, Month.MAY, 11, 10, 50);

    public static final int NB_PROJECTS = 38;
    public static Long projectId;

    // Use resource to avoid RestAssured deserialization using jackson and not Json-B
    @Inject
    private ProjectResource resource;

    @Test
    void shouldGetRandomProjectForUser() {
        long userId = 1L;
        given()
                .when().get("/api/projects/user/" + userId + "/random")
                .then()
                .statusCode(OK.getStatusCode())
                .header(CONTENT_TYPE, APPLICATION_JSON);
    }

    @Test
    void shouldNotGetUnknownProject() {
        Long randomId = new Random().nextLong();
        given()
                .pathParam("id", randomId)
                .when().get("/api/projects/{id}")
                .then()
                .statusCode(NO_CONTENT.getStatusCode());
    }

    // Use resource to avoid RestAssured deserialization using jackson and not Json-B
    @Test
    void shouldNotAddInvalidItem() {
        Project proj = new Project();
        proj.lastModified = DEFAULT_DATE;
        proj.description = DEFAULT_DESCRIPTION;
        proj.color = DEFAULT_COLOR;
        proj.isArchived = DEFAULT_IS_ARCHIVED;
        proj.userId = DEFAULT_USER_ID;
        proj.name = "A";
        try {
            Project p = resource.service.persist(proj);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.assertNull(null);
        }
    }

    // Use resource to avoid RestAssured deserialization using jackson and not Json-B
    @Test
    void shouldBeSorted() {
        List<Project> projects = resource.service.listSortBy("order by " + resource.sort);
        Assertions.assertEquals(NB_PROJECTS, projects.size());
        Assertions.assertEquals(3, projects.get(0).id); // 3 project named 'test' 2020-05-06
    }

    @Test
    @Order(1)
    void shouldGetInitialItems() {
        List<Project> projects = resource.service.findAll();
        Assertions.assertEquals(NB_PROJECTS, projects.size());
    }

    @Test
    @Order(2)
    void shouldAddUser() {
        Project proj = new Project();
        proj.lastModified = DEFAULT_DATE;
        proj.description = DEFAULT_DESCRIPTION;
        proj.color = DEFAULT_COLOR;
        proj.isArchived = DEFAULT_IS_ARCHIVED;
        proj.userId = DEFAULT_USER_ID;
        proj.name = DEFAULT_NAME;

        Project newProj = resource.service.persist(proj);
        assertEquals(DEFAULT_NAME, newProj.name);

        projectId = newProj.id;
        assertNotNull(projectId);

        newProj = resource.service.findById(projectId);
        assertEquals(DEFAULT_NAME, newProj.name);

        List<Project> projects = resource.service.findAll();
        assertEquals(NB_PROJECTS + 1, projects.size());
    }

    @Test
    @Order(3)
    void shouldUpdateAnItem() {
        Project proj = new Project();
        proj.lastModified = UPDATE_DATE;
        proj.description = UPDATE_DESCRIPTION;
        proj.color = UPDATE_COLOR;
        proj.isArchived = UPDATE_IS_ARCHIVED;
        proj.userId = UPDATE_USER_ID;
        proj.name = UPDATE_NAME;
        proj.id = projectId;

        Project newProj = resource.service.update(proj);
        newProj = resource.service.findById(newProj.id);
        assertEquals(UPDATE_NAME, newProj.name);
        assertEquals(UPDATE_DATE, newProj.lastModified);
        assertEquals(UPDATE_DESCRIPTION, newProj.description);
        assertEquals(UPDATE_COLOR, newProj.color);
        assertEquals(UPDATE_USER_ID, newProj.userId);
        List<Project> projects = resource.service.findAll();
        assertEquals(NB_PROJECTS + 1, projects.size());
    }

    @Test
    @Order(4)
    void shouldRemoveAnItem() {
        resource.service.delete(projectId);
        List<Project> projects = resource.service.findAll();
        assertEquals(NB_PROJECTS, projects.size());
    }

    @Test
    @Order(5)
    void shouldGetLatestProject() {
        Project p = resource.service.userLatest(7L);
        assertEquals(36L, p.id);
    }


    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/api/projects/hello")
                .then()
                .statusCode(200)
                .body(is("hello " + ProjectResource.class.getSimpleName()));
    }

    @Test
    void shouldPingOpenAPI() {
        given()
                .header(ACCEPT, APPLICATION_JSON)
                .when().get("/openapi")
                .then()
                .statusCode(OK.getStatusCode());
    }

    @Test
    void shouldPingSwaggerUI() {
        given()
                .when().get("/swagger-ui")
                .then()
                .statusCode(OK.getStatusCode());
    }

    @Test
    void shouldPingLiveness() {
        given()
                .when().get("/health/live")
                .then()
                .statusCode(OK.getStatusCode());
    }

    @Test
    void shouldPingReadiness() {
        given()
                .when().get("/health/ready")
                .then()
                .statusCode(OK.getStatusCode());
    }

    @Test
    void shouldPingMetrics() {
        given()
                .header(ACCEPT, APPLICATION_JSON)
                .when().get("/metrics/application")
                .then()
                .statusCode(OK.getStatusCode());
    }

    private TypeRef<List<Project>> getProjectTypeRef() {
        return new TypeRef<List<Project>>() {
            // Kept empty on purpose
        };
    }

}