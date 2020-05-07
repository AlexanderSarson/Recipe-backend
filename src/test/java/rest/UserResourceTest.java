package rest;
/*
 * author mads
 * version 1.0
 */

import com.google.gson.Gson;
import dtos.favourites.FavouriteRecipeDTO;
import entity.FavouriteRecipe;
import entity.User;
import io.restassured.http.ContentType;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import utils.EMF_Creator;
import io.restassured.RestAssured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.restassured.parsing.Parser;

import java.net.URI;
import java.util.Arrays;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;


public class UserResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static User u1, u2;
    private String nonExistingUsername = "nonexistinguser";
    private FavouriteRecipeDTO nonExistingRecipeForU2 = new FavouriteRecipeDTO(555L, "I Dont exist on user 2", "url", 1, 1);
    private static FavouriteRecipeDTO u2Favourite = new FavouriteRecipeDTO(4321L, "Tortilla", "url", 20, 3);

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.DROP_AND_CREATE);

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;

        u1 = new User("reactLover", "loveReact");
        u2 = new User("jpaLover", "loveJpa");
        u2.addFavourite(new FavouriteRecipe(u2Favourite));

    }

    @AfterAll
    public static void closeTestServer() {
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.createNamedQuery("favourite_recipe.deleteAllRows").executeUpdate();
            em.createNativeQuery("DELETE FROM startcode_test.user_favourites").executeUpdate();
            em.persist(u1);
            em.persist(u2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        given().when().get("/user").then().statusCode(200);
    }

    @Test
    public void testAddFavourite() {
        String username = u1.getUserName();
        FavouriteRecipeDTO favdto = new FavouriteRecipeDTO(1234L, "Pancake", "someImgUrl", 45, 2);
        JSONObject body = new JSONObject();
        body.put("username", username);
        body.put("recipe", favdto);
        body.put("action", "add");

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/user/favourites")
                .then().statusCode(200)
                .assertThat()
                .body("username", equalTo(username))
                .body("favouriteRecipes", hasSize(1))
                .body("favouriteRecipes[0].id", equalTo(1234))
                .body("favouriteRecipes[0].title", equalTo("Pancake"))
                .body("favouriteRecipes[0].image", equalTo("someImgUrl"))
                .body("favouriteRecipes[0].readyInMinutes", equalTo(45))
                .body("favouriteRecipes[0].servings", equalTo(2));
    }

    @Test
    public void testAddFavourite_withNonExistingUser() {
        FavouriteRecipeDTO favdto = new FavouriteRecipeDTO(1234L, "Pancake", "someImgUrl", 45, 2);
        JSONObject body = new JSONObject();
        body.put("username", nonExistingUsername);
        body.put("recipe", favdto);
        body.put("action", "add");

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/user/favourites")
                .then().statusCode(406)
                .assertThat()
                .body("message", equalTo("User not found"));
    }

    @Test
    public void testGetFavourites() {
        String username = u2.getUserName();
        given().when().get("user/favourites/" + username)
                .then().assertThat()
                .statusCode(200).and()
                .body("favouriteRecipes", hasSize(1));
    }

    @Test
    public void testGetFavourites_ForNonExistingUser () {
        given().when().get("user/favourites/" + nonExistingUsername)
                .then().assertThat()
                .statusCode(406)
                .body("message", equalTo("User not found"));
    }

    @Test
    public void testRemoveFavourite() {
        String username = u2.getUserName();
        JSONObject body = new JSONObject();
        body.put("username", username);
        body.put("recipe", u2Favourite);
        body.put("action", "remove");

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/user/favourites")
                .then().statusCode(200)
                .assertThat()
                .body("favouriteRecipes", hasSize(0));
    }

    @Test
    public void testRemoveFavourite_withNonExistingUser () {
        JSONObject body = new JSONObject();
        body.put("username", nonExistingUsername);
        body.put("recipe", u2Favourite);
        body.put("action", "remove");

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/user/favourites")
                .then().statusCode(406)
                .assertThat()
                .body("message", equalTo("User not found"));
    }

    @Test
    public void testRemoveFavourite_withNonExistingRecipe () {
        JSONObject body = new JSONObject();
        String username = u2.getUserName();
        body.put("username", username);
        body.put("recipe", nonExistingRecipeForU2);
        body.put("action", "remove");

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/user/favourites")
                .then().statusCode(500)
                .assertThat()
                .body("message", equalTo("Internal Server Error"));
    }
}
