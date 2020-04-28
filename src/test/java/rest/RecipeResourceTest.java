package rest;

import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

public class RecipeResourceTest extends BaseResourceTest {

    @Test
    void test_searchForRecipe() {
        String payload = "{name:\"Falafel Burgers with Feta Cucumber Sauce\", number:1}";
        given()
                .contentType("application/json")
                .body(payload)
                .post("recipe/search")
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("results", hasSize(1));
    }
    
    @Test
    void test_getRandomRecipe() {
        given()
                .contentType("application/json")
                .get("recipe/random/5")
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("results", hasSize(5));
    }
}