package rest;

import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class RecipeResourceTest extends BaseResourceTest {
    
    @Test
    public void test_searchForRecipe() {
        String payload = "{search:\"Falafel Burgers with Feta Cucumber Sauce\", number:1}";
        given()
                .contentType("application/json")
                .body(payload)
                .post("recipe/search")
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("results", hasSize(1));
    }

    @Test
    public void test_searchForRecipe_with_odd_input() {
        String payload = "{search:\"Falafel\",excludeIngredients:\"unbleached all purpose flour,salt,bay leaf\",includeIngredients:\"hummus\",number:4}";
        given()
                .contentType("application/json")
                .body(payload)
                .post("recipe/search")
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("results", hasSize(4));
    }

    public void test_searchForRecipe_with_cuisine() {
        String payload = "{search:\"burger\",excludeIngredients:\"\",includeIngredients:\"\",excludeCuisine:\"british\",cuisine:\"american\",number:4}";
        given()
                .contentType("application/json")
                .body(payload)
                .post("recipe/search")
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("results", hasSize(4));
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
    
    
    @Test
    void test_getRecipeById() {
        given()
                .contentType("application/json")
                .get("recipe/id/324694")
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", is(324694));
    }
}