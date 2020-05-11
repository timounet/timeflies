package org.timeflies.projects;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.CoreMatchers.is;

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
    public static final String DEFAULT_DATE = "2019-05-12 12:46:17";
    public static final String UPDATE_DATE = "2020-05-11 12:46:17";

    public static final int NB_PROJECTS = 38;
    public static String projectId;

    @BeforeAll
    static void giveMeAMapper() {
        final Jsonb jsonb = JsonbBuilder.create();
        ObjectMapper mapper = new ObjectMapper() {
            @Override
            public Object deserialize(ObjectMapperDeserializationContext context) {
                return jsonb.fromJson(context.getDataToDeserialize().asString(), context.getType());
            }

            @Override
            public Object serialize(ObjectMapperSerializationContext context) {
                return jsonb.toJson(context.getObjectToSerialize());
            }
        };
        RestAssured.config.objectMapperConfig(ObjectMapperConfig.objectMapperConfig().defaultObjectMapper(mapper));
    }

    @Test
    void shouldGetRandomProjectForUser() {
        Long userId = 1L;
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

   /* @Test
    void shouldNotAddInvalidItem() throws ParseException {
        Project proj = new Project();
        proj.lastModified = LocalDateTime.parse(DEFAULT_DATE);
        proj.description = DEFAULT_DESCRIPTION;
        proj.color = DEFAULT_COLOR;
        proj.isArchived = DEFAULT_IS_ARCHIVED;
        proj.userId = DEFAULT_USER_ID;
        proj.name = "A";

        given()
                .body(proj)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(ACCEPT, APPLICATION_JSON)
                .when()
                .post("/api/projects")
                .then()
                .statusCode(BAD_REQUEST.getStatusCode());
    }*/
/*
    @Test
    void shouldBeSorted() {
        List<Project> projects = get("/api/projects/sort").then()
                .statusCode(OK.getStatusCode())
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                .extract().body().as(getProjectTypeRef());
        assertEquals(NB_PROJECTS, projects.size());
        assertEquals(3, projects.get(0).id); // 3 project named 'test' 2020-05-06
    }

    @Test
    @Order(1)
    void shouldGetInitialItems() {
        List<Project> projects = get("/api/projects").then()
                .statusCode(OK.getStatusCode())
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                .extract().body().as(getProjectTypeRef());
        assertEquals(NB_PROJECTS, projects.size());
    }*/

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