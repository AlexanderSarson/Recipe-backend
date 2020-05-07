package facades;

import dtos.RecipeDTO;
import dtos.RecipeDTOList;
import dtos.favourites.FavouriteRecipeDTO;
import dtos.favourites.FavouriteRecipeDtoList;
import entity.FavouriteRecipe;
import session.SessionOffsetManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

public class StatisticFacade {
    private static EntityManagerFactory emf;
    private static StatisticFacade instance;

    private static final String MOST_POPULAR ="select fr.recipe_id, imgUrl, readyInMinutes, servings, title from favourite_recipe as fr join (select recipe_id, count(*) as count from user_favourites group by recipe_id) as counts on counts.recipe_id = fr.recipe_id order by count desc;";

    private StatisticFacade(){}

    /**
     * @param entityManagerFactory
     * @return the instance of this facade.
     */
    public static StatisticFacade getStatisticFacade (EntityManagerFactory entityManagerFactory) {
        if (instance == null) {
            emf = entityManagerFactory;
            instance = new StatisticFacade();
        }
        return instance;
    }

    public FavouriteRecipeDtoList getMostPopular() {
        EntityManager entityManager = emf.createEntityManager();
        FavouriteRecipeDtoList results = new FavouriteRecipeDtoList();
        List<FavouriteRecipe> recipes = new ArrayList<>();
        try {
            entityManager.getTransaction().begin();
             recipes = entityManager.createNativeQuery(MOST_POPULAR, FavouriteRecipe.class).getResultList();
            entityManager.getTransaction().commit();
        } catch(Exception e ) {
            e.printStackTrace();
        }finally {
            entityManager.close();
        }
        recipes.forEach(favouriteRecipe -> results.addRecipeToList(new FavouriteRecipeDTO(favouriteRecipe)));
        return results;
    }

    public FavouriteRecipeDtoList getMostTrending() {
        return null;
    }
}
