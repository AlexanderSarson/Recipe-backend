package facades;

import dtos.UserDto;
import dtos.favourites.FavouriteRecipeDTO;
import dtos.favourites.FavouriteRecipeDtoList;
import entity.FavouriteRecipe;
import entity.Role;
import entity.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import errorhandling.AuthenticationException;
import errorhandling.RecipeException;
import errorhandling.UserException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {
  
    private static EntityManagerFactory emf;
    private static UserFacade instance;
    
    public UserFacade(){}
    
    /**
     * 
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade (EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User createUser(String username, String password) throws UserException {
        List<Role> roleList = new ArrayList<>();
        roleList.add(new Role("user"));
        User user = new User(username,password);
        user.setRoleList(roleList);
        EntityManager entityManager = emf.createEntityManager();
        try {
            boolean notInUse = (entityManager.find(User.class, username) == null);
            if(notInUse) {
                entityManager.getTransaction().begin();
                entityManager.persist(user);
                entityManager.getTransaction().commit();
            }
            else throw new UserException(UserException.IN_USE_USERNAME);
        }
        finally {
            entityManager.close();
        }
        return user;
    }

    public User getVerifiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public UserDto addFavourite (String username, FavouriteRecipeDTO recipeDTO) throws UserException {
        EntityManager em = emf.createEntityManager();
        User user;
        FavouriteRecipe recipe;
        try {
            user = em.find(User.class, username);
            if (user == null) {
                throw new UserException(UserException.USER_NOT_FOUND);
            }
            recipe = em.find(FavouriteRecipe.class, recipeDTO.getId());

            if (recipe == null) {
                recipe = new FavouriteRecipe(recipeDTO);
            }
            user.addFavourite(recipe);
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new UserDto(user);
    }

    public UserDto removeFavourite(String username, FavouriteRecipeDTO favRecipeDTO) throws UserException, RecipeException {
        EntityManager em = emf.createEntityManager();
        User user;
        FavouriteRecipe recipe;
        try {
            user = em.find(User.class, username);
            if (user == null) {
                throw new UserException(UserException.USER_NOT_FOUND);
            }
            recipe = em.find(FavouriteRecipe.class, favRecipeDTO.getId());
            if (recipe == null) {
                throw new RecipeException(RecipeException.RECIPE_NOT_FOUND);
            }
            user.removeFavourite(recipe);
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new UserDto(user);
    }

    public FavouriteRecipeDtoList getFavourites (String username) throws UserException {
        EntityManager em = emf.createEntityManager();
        User user;
        FavouriteRecipeDtoList favouriteRecipeDTOList = new FavouriteRecipeDtoList();
        try {
            user = em.find(User.class, username);
            if (user == null) {
                throw new UserException(UserException.USER_NOT_FOUND);
            }
            List<FavouriteRecipe> favouriteRecipes = user.getFavourites();
            for (FavouriteRecipe recipe : favouriteRecipes) {
                favouriteRecipeDTOList.addRecipeToList(new FavouriteRecipeDTO(recipe));
            }
            return favouriteRecipeDTOList;
        } finally {
            em.close();
        }
    }
}
