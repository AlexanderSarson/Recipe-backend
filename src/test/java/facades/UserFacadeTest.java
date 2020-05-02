package facades;

import dtos.UserDto;
import dtos.favourites.FavouriteRecipeDTO;
import entity.User;
import errorhandling.RecipeException;
import errorhandling.UserException;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserFacadeTest {

    private static EntityManagerFactory entityManagerFactory;
    private static UserFacade userFacade;
    private static User u1, u2;
    private static FavouriteRecipeDTO favRecipeDTO;

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
    void test_addFavourite() throws UserException {
        int expected = 1;
        UserDto userDto = userFacade.addFavourite(u2.getUserName(), favRecipeDTO);
        int result = userDto.getFavouriteRecipes().size();
        assertEquals(expected, result);
    }

    @Test
    void test_addFavouriteToNonExistingUser_throwsUserException() {
        Assertions.assertThrows(UserException.class, () -> userFacade.addFavourite("user3", favRecipeDTO));
    }

    @Test
    void test_removeFavourite() throws RecipeException, UserException {
        int expected = 0;
        FavouriteRecipeDTO favDto = new FavouriteRecipeDTO(12345L, "Pancake", "someImgUrl", 45, 2);
        UserDto userDto = userFacade.addFavourite(u1.getUserName(), favDto);
        userDto = userFacade.removeFavourite(userDto.getUsername(), favDto);
        int result = userDto.getFavouriteRecipes().size();
        assertEquals(expected, result);
    }

    @Test
    void test_getFavouriteRecipeList() throws UserException {
        int expected = 1;
        UserDto userDto = userFacade.addFavourite(u1.getUserName(), favRecipeDTO);
        int result = userFacade.getFavourites(userDto.getUsername()).getSize();
        assertEquals(expected, result);
    }

    @Test
    void test_getFavouritesForNonExistingUser_shouldThrowException() {
        Assertions.assertThrows(UserException.class, () -> userFacade.getFavourites("user3"));
    }
}