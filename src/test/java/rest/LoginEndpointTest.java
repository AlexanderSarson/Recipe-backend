package rest;

import entity.Role;
import entity.User;
import errorhandling.UserException;
import io.restassured.http.ContentType;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class LoginEndpointTest extends BaseResourceTest {

    @BeforeEach
    public void setUp() {
        Role role = new Role("user");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(role);
        User user = new User("testUser", "testPassword");
        user.setRoleList(userRoles);
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createNamedQuery("User.deleteAllRows").executeUpdate();
        entityManager.createNamedQuery("Roles.deleteAllRows").executeUpdate();
        entityManager.persist(role);
        entityManager.persist(user);
        entityManager.getTransaction().commit();
    }

    @Test
    public void testLogin_with_incorrect_password() {
        String payload ="{\"username\":\"testUser\",\"password\":\"testPasswordZZ\"}";
        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("login")
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN_403.getStatusCode())
                .body("message", equalTo("Invalid user name or password"));
    }

    @Test
    public void testCreate_with_incorrect_data() {
        String payload = "{\"password\":\"blablabla\"}";
        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("login/create")
                .then()
                .statusCode(HttpStatus.NOT_ACCEPTABLE_406.getStatusCode())
                .body("message", equalTo("Could not create user"));
    }

    @Test
    public void testCreate_with_duplicate_username() {
        String payload = "{\"username\":\"testUser\",\"password\":\"this is actually not the real password \"}";
        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("login/create")
                .then()
                .statusCode(HttpStatus.NOT_ACCEPTABLE_406.getStatusCode())
                .body("message", equalTo(UserException.IN_USE_USERNAME));
    }

    @Test
    public void testLogin_with_correct_password() {
        String payload = "{\"username\":\"testUser\",\"password\":\"testPassword\"}";
        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("login")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("token", notNullValue());
    }

    @Test
    public void testAccess_non_existing_page() {
        String page = "logins";
        given()
                .contentType(ContentType.JSON)
                .get(page)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("message", equalTo("HTTP 404 Not Found"));
    }
}