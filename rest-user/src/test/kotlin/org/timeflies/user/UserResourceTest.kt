package org.timeflies.user

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test

@QuarkusTest
class UserResourceTest {

    @Test
    fun testHelloEndpoint() {
        given()
          .`when`().get("/api/users")
          .then()
             .statusCode(200)
             .body(`is`("hello"))
    }

}