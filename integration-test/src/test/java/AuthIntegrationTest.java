import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static org.hamcrest.Matchers.notNullValue;

public class AuthIntegrationTest {

    @BeforeAll
    static void setUp(){
        RestAssured.baseURI = "http://localhost:4004";
    }


    @Test
    public void shouldReturnOKWithValidToken(){
        String loggingPayload = """
                    {
                        "email": "testuser@test.com",
                        "password": "password123"
                    }
                """;

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(loggingPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract()
                .response();

        System.out.println("Generated token: " + response.jsonPath().getString("token"));
    }


    @Test
    public void shouldReturnUnauthorizedWithInvalidToken(){
        String loggingPayload = """
                    {
                        "email": "invaliduser@test.com",
                        "password": "password"
                    }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(loggingPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);


    }

}
