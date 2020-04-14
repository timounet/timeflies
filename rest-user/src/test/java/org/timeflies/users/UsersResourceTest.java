package org.timeflies.users;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.ws.rs.core.HttpHeaders;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(DatabaseResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsersResourceTest {
    private static final String DEFAULT_USER_NAME = "spaceExplorer";
    private static final String UPDATED_USER_NAME = "spaceExplorer (updated)";
    private static final String DEFAULT_FIRST_NAME = "Roger";
    private static final String UPDATED_FIRST_NAME = "Roger (updated)";
    private static final String DEFAULT_LAST_NAME = "Cosmik";
    private static final String UPDATED_LAST_NAME = "Comsik (updated)";
    private static final String DEFAULT_PICTURE_URL = "galaxy_looser.png";
    private static final String UPDATED_PICTURE_URL = "galaxy_looser.png (updated)";
    private static final String DEFAULT_STATUS = "galaxy looser";
    private static final String UPDATED_STATUS = "galaxy looser (updated)";

    private static final int NB_USERS = 7;
    private static String userId;

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/api/users/hello")
                .then()
                .statusCode(200)
                .body(is("hello " + UsersResource.class.getSimpleName()));
    }

    @Test
    void shouldNotGetUnknownUser() {
        Long randomId = new Random().nextLong();
        given()
                .pathParam("id", randomId)
                .when().get("/api/users/{id}")
                .then()
                .statusCode(NO_CONTENT.getStatusCode());
    }

    @Test
    void shouldGetRandomUser() {
        given()
                .when().get("/api/users/random")
                .then()
                .statusCode(OK.getStatusCode())
                .header(CONTENT_TYPE, APPLICATION_JSON);
    }

    @Test
    void shouldNotAddInvalidItem() {
        Users user = new Users();
        user.userName = null;
        user.firstName = DEFAULT_FIRST_NAME;
        user.lastName = DEFAULT_LAST_NAME;
        user.pictureUrl = DEFAULT_PICTURE_URL;
        user.status = DEFAULT_STATUS;

        given()
                .body(user)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(ACCEPT, APPLICATION_JSON)
                .when()
                .post("/api/users")
                .then()
                .statusCode(BAD_REQUEST.getStatusCode());
    }

    @Test
    void shouldBeSorted() {
        List<Users> users = get("/api/users/sort").then()
                .statusCode(OK.getStatusCode())
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                .extract().body().as(getUsersTypeRef());
        assertEquals(NB_USERS, users.size());
        assertEquals(5, users.get(0).id); // baboulinet is the 1st
    }

    @Test
    @Order(1)
    void shouldGetInitialItems() {
        List<Users> users = get("/api/users").then()
                .statusCode(OK.getStatusCode())
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                .extract().body().as(getUsersTypeRef());
        assertEquals(NB_USERS, users.size());
    }

    @Test
    @Order(2)
    void shouldAddUser() {
        Users user = new Users();
        user.userName = DEFAULT_USER_NAME;
        user.firstName = DEFAULT_FIRST_NAME;
        user.lastName = DEFAULT_LAST_NAME;
        user.pictureUrl = DEFAULT_PICTURE_URL;
        user.status = DEFAULT_STATUS;

        String location = given()
                .body(user)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(ACCEPT, APPLICATION_JSON)
                .when()
                .post("/api/users")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract().header("Location");
        assertTrue(location.contains("/api/users"));

        // Stores the id
        String[] segments = location.split("/");
        userId = segments[segments.length - 1];
        assertNotNull(userId);

        given()
                .pathParam("id", userId)
                .when().get("/api/users/{id}")
                .then()
                .statusCode(OK.getStatusCode())
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .body("userName", Is.is(DEFAULT_USER_NAME))
                .body("firstName", Is.is(DEFAULT_FIRST_NAME))
                .body("lastName", Is.is(DEFAULT_LAST_NAME))
                .body("pictureUrl", Is.is(DEFAULT_PICTURE_URL))
                .body("status", Is.is(DEFAULT_STATUS));

        List<Users> heroes = get("/api/users").then()
                .statusCode(OK.getStatusCode())
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .extract().body().as(getUsersTypeRef());
        assertEquals(NB_USERS + 1, heroes.size());
    }

    @Test
    @Order(3)
    void shouldUpdateAnItem() {
        Users user = new Users();
        user.id = Long.valueOf(userId);
        user.userName = UPDATED_USER_NAME;
        user.firstName = UPDATED_FIRST_NAME;
        user.lastName = UPDATED_LAST_NAME;
        user.pictureUrl = UPDATED_PICTURE_URL;
        user.status = UPDATED_STATUS;

        given()
                .body(user)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(ACCEPT, APPLICATION_JSON)
                .when()
                .put("/api/users")
                .then()
                .statusCode(OK.getStatusCode())
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .body("userName", Is.is(UPDATED_USER_NAME))
                .body("firstName", Is.is(UPDATED_FIRST_NAME))
                .body("lastName", Is.is(UPDATED_LAST_NAME))
                .body("pictureUrl", Is.is(UPDATED_PICTURE_URL))
                .body("status", Is.is(UPDATED_STATUS));

        List<Users> users = get("/api/users").then()
                .statusCode(OK.getStatusCode())
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .extract().body().as(getUsersTypeRef());
        assertEquals(NB_USERS + 1, users.size());
    }

    @Test
    @Order(4)
    void shouldRemoveAnItem() {
        given()
                .pathParam("id", userId)
                .when().delete("/api/users/{id}")
                .then()
                .statusCode(NO_CONTENT.getStatusCode());

        List<Users> users = get("/api/users").then()
                .statusCode(OK.getStatusCode())
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .extract().body().as(getUsersTypeRef());
        assertEquals(NB_USERS, users.size());
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

    private TypeRef<List<Users>> getUsersTypeRef() {
        return new TypeRef<List<Users>>() {
            // Kept empty on purpose
        };
    }

}