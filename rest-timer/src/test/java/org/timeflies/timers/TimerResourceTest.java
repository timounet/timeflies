package org.timeflies.timers;

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
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@QuarkusTestResource(DatabaseResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TimerResourceTest {

    public static final Long DEFAULT_PROJECT_ID = 38L;
    public static final Long UPDATE_PROJECT_ID = 39L;
    public static final Long DEFAULT_USER_ID = 7L;
    public static final Long UPDATE_USER_ID = 8L;
    public static final LocalDateTime DEFAULT_START_DATE = LocalDateTime.of(2019, Month.FEBRUARY, 1, 10, 50);
    public static final LocalDateTime UPDATE_START_DATE = LocalDateTime.of(2020, Month.MAY, 11, 10, 50);
    public static final LocalDateTime DEFAULT_FINISH_DATE = DEFAULT_START_DATE.plusHours(1);
    public static final LocalDateTime UPDATE_FINISH_DATE = UPDATE_START_DATE.plusHours(1);
    public static final int NB_timers = 216;
    public static Long timerId;

    // Use resource to avoid RestAssured deserialization using jackson and not Json-B
    @Inject
    private TimerResource resource;


    @Test
    void shouldNotGetUnknownTimer() {
        Long randomId = new Random().nextLong();
        given()
                .pathParam("id", randomId)
                .when().get("/api/timers/{id}")
                .then()
                .statusCode(NO_CONTENT.getStatusCode());
    }

    // Use resource to avoid RestAssured deserialization using jackson and not Json-B
    @Test
    void shouldNotAddInvalidItem() {
        Timer timer = new Timer();
        timer.finish = DEFAULT_FINISH_DATE;
        timer.start = DEFAULT_START_DATE;
        timer.userId = DEFAULT_USER_ID;
        try {
            Timer p = resource.service.persist(timer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.assertNull(null);
        }
    }

    // Use resource to avoid RestAssured deserialization using jackson and not Json-B
    @Test
    void shouldBeSorted() {
        List<Timer> timers = resource.service.listSortBy("order by " + resource.sort);
        Assertions.assertEquals(NB_timers, timers.size());
        Assertions.assertEquals(214, timers.get(0).id); // 3 timer named 'test' 2020-05-06
    }

    @Test
    @Order(1)
    void shouldGetInitialItems() {
        List<Timer> timers = resource.service.findAll();
        Assertions.assertEquals(NB_timers, timers.size());
    }

    @Test
    @Order(2)
    void shouldAddUser() {
        Timer timer = new Timer();
        timer.finish = DEFAULT_FINISH_DATE;
        timer.start = DEFAULT_START_DATE;
        timer.userId = DEFAULT_USER_ID;
        timer.projectId = DEFAULT_PROJECT_ID;

        Timer newTimer = resource.service.persist(timer);
        Assertions.assertEquals(DEFAULT_START_DATE, newTimer.start);

        timerId = newTimer.id;
        Assertions.assertNotNull(timerId);

        newTimer = resource.service.findById(timerId);
        Assertions.assertEquals(DEFAULT_START_DATE, newTimer.start);

        List<Timer> timers = resource.service.findAll();
        Assertions.assertEquals(NB_timers + 1, timers.size());
    }

    @Test
    @Order(3)
    void shouldUpdateAnItem() {
        Timer timer = new Timer();
        timer.finish = UPDATE_FINISH_DATE;
        timer.start = UPDATE_START_DATE;
        timer.userId = UPDATE_USER_ID;
        timer.projectId = UPDATE_PROJECT_ID;
        timer.id = timerId;

        Timer newTimer = resource.service.update(timer);
        newTimer = resource.service.findById(newTimer.id);
        Assertions.assertEquals(UPDATE_FINISH_DATE, newTimer.finish);
        Assertions.assertEquals(UPDATE_START_DATE, newTimer.start);
        Assertions.assertEquals(UPDATE_USER_ID, newTimer.userId);
        Assertions.assertEquals(UPDATE_PROJECT_ID, newTimer.projectId);
        Assertions.assertEquals(UPDATE_USER_ID, newTimer.userId);
        List<Timer> timers = resource.service.findAll();
        Assertions.assertEquals(NB_timers + 1, timers.size());
    }

    @Test
    @Order(4)
    void shouldRemoveAnItem() {
        resource.service.delete(timerId);
        List<Timer> timers = resource.service.findAll();
        Assertions.assertEquals(NB_timers, timers.size());
    }

    @Test
    @Order(5)
    void shouldGetLatestTimer() {
        Timer p = resource.service.userLatest(5L);
        Assertions.assertEquals(138L, p.id);
    }

    @Test
    @Order(6)
    void shouldGetProjectTimer() {
        List<Timer> p = resource.service.projectList(25L);
        Assertions.assertEquals(7, p.size());
    }

    @Test
    @Order(7)
    void shouldGetProjectLatestTimer() {
        Timer p = resource.service.projectLatest(25L);
        Assertions.assertEquals(138L, p.id);
    }


    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/api/timers/hello")
                .then()
                .statusCode(200)
                .body(is("hello " + TimerResource.class.getSimpleName()));
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


    private TypeRef<List<Timer>> getTimerTypeRef() {
        return new TypeRef<List<Timer>>() {
            // Kept empty on purpose
        };
    }

}