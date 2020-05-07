package facades;

import dtos.RecipeDTOList;
import dtos.favourites.FavouriteRecipeDTO;
import dtos.favourites.FavouriteRecipeDtoList;
import entity.FavouriteRecipe;
import entity.User;
import errorhandling.UserException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

class StatisticFacadeTest {
    private static EntityManagerFactory entityManagerFactory;
    private static UserFacade userFacade;
    private static StatisticFacade statisticFacade;
    private static User u1, u2, u3;
    private static FavouriteRecipeDTO favRecipeDTO, favRecipeDTO2, favRecipeDTO3;

    @BeforeAll
    public static void setUpClass() {
        entityManagerFactory = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.DROP_AND_CREATE);
        userFacade = UserFacade.getUserFacade(entityManagerFactory);
        statisticFacade = StatisticFacade.getStatisticFacade(entityManagerFactory);
        u1 = new User("test1", "test1");
        u2 = new User("jpaLover", "loveJpa");
        u3 = new User("user3", "user3Password");
        favRecipeDTO = new FavouriteRecipeDTO(1L, "Pancake", "someImgUrl", 45, 2);
        favRecipeDTO2 = new FavouriteRecipeDTO(2L, "Pancake", "someImgUrl", 45, 2);
        favRecipeDTO3 = new FavouriteRecipeDTO(3L, "Pancake", "someImgUrl", 45, 2);
    }

    @BeforeEach
    public void setUp() throws UserException {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.createNamedQuery("Roles.deleteAllRows").executeUpdate();
            em.createNamedQuery("favourite_recipe.deleteAllRows").executeUpdate();
            em.createNativeQuery("DELETE FROM startcode_test.user_favourites").executeUpdate();
            em.persist(u1);
            em.persist(u2);
            em.persist(u3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        userFacade.addFavourite(u1.getUserName(),favRecipeDTO);
        userFacade.addFavourite(u1.getUserName(),favRecipeDTO2);
        userFacade.addFavourite(u1.getUserName(),favRecipeDTO3);

        userFacade.addFavourite(u2.getUserName(),favRecipeDTO);
        userFacade.addFavourite(u2.getUserName(),favRecipeDTO2);

        userFacade.addFavourite(u3.getUserName(),favRecipeDTO2);

    }

    @Test
    public void testGetMostPopular() {
        FavouriteRecipeDtoList recipes = statisticFacade.getMostPopular();
        assertEquals(3, recipes.getFavouriteRecipes().size());
        assertEquals(favRecipeDTO2.getId(), recipes.getFavouriteRecipes().get(0).getId());
        assertEquals(favRecipeDTO.getId(), recipes.getFavouriteRecipes().get(1).getId());
        assertEquals(favRecipeDTO3.getId(), recipes.getFavouriteRecipes().get(2).getId());
    }
}