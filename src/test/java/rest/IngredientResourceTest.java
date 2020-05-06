package rest;

import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

public class IngredientResourceTest extends BaseResourceTest {
    @Test
    public void autoCompleteIngredient_with_valid_payload() {
        String payload = "app";
        given()
                .get("ingredient/autocomplete/" + payload)
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("name", hasItems("apple", "applesauce", "apple cider", "apple juice", "apple jelly"));
    }

    @Test
    public void autoCompleteIngredient_with_invalid_payload() {
        String payload = "akjaskljdlakjdlkjajlkdj";
        given()
                .get("ingredient/autocomplete/" + payload)
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("", hasSize(0));
    }
}