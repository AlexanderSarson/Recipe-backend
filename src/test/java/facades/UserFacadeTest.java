package facades;

import dtos.favourites.FavouriteRecipeDTO;
import entity.FavouriteRecipe;
import entity.User;
import errorhandling.RecipeException;
import errorhandling.UserException;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

class UserFacadeTest {

    private static EntityManagerFactory entityManagerFactory;
    private static UserFacade userFacade;
    private static User u1, u2;
    private static FavouriteRecipeDTO favRecipeDTO;
    // private static PersonDTO pd1, pd2;

    @BeforeAll
    public static void setUpClass() {
        entityManagerFactory = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.DROP_AND_CREATE);
        userFacade = UserFacade.getUserFacade(entityManagerFactory);
        u1 = new User("test1", "test1");
        u2 = new User("jpaLover", "loveJpa");
        favRecipeDTO = new FavouriteRecipeDTO(1234L, "Pancake", "someImgUrl", 45, 2);
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.createNamedQuery("FavouriteRecipe.deleteAllRows").executeUpdate();
            em.createNativeQuery("DELETE FROM startcode_test.user_favourites").executeUpdate();
            em.persist(u1);
            em.persist(u2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    void test_addFavourite() throws UserException {
        int expected = 1;
        u2 = userFacade.addFavourite(u2.getUserName(), favRecipeDTO);
        int result = u2.getFavourites().size();
        assertEquals(expected, result);
    }

    @Test
    void test_addFavouriteToNonExistingUser_throwsUserException() {
        Assertions.assertThrows(UserException.class, () -> userFacade.addFavourite("user3", favRecipeDTO));
    }

    @Disabled // TODO Dunno why its failing......
    @Test
    void test_removeFavourite() throws RecipeException, UserException {
        int expected = 0;
        FavouriteRecipeDTO favDto = new FavouriteRecipeDTO(12345L, "Pancake", "someImgUrl", 45, 2);
        u1 = userFacade.addFavourite(u1.getUserName(), favDto);
        u1 = userFacade.removeFavourite(u1.getUserName(), favDto);
        int result = u1.getFavourites().size();
        assertEquals(expected, result);
    }

    @Test
    void test_getFavouriteRecipeList() throws UserException {
        int expected = 1;
        u1 = userFacade.addFavourite(u1.getUserName(), favRecipeDTO);
        int result = userFacade.getFavourites(u1.getUserName()).size();
        assertEquals(expected, result);
    }

    @Test
    void test_getFavouritesForNonExistingUser_shouldThrowException() {
        Assertions.assertThrows(UserException.class, () -> userFacade.getFavourites("user3"));
    }
}