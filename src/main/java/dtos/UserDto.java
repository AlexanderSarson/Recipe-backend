package dtos;

import dtos.favourites.FavouriteRecipeDTO;
import entity.FavouriteRecipe;
import entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserDto {
    private String username;
    private List<FavouriteRecipeDTO> favouriteRecipes;

    public UserDto() {}

    public UserDto(String username) {
        this.username = username;
    }

    public UserDto(User user) {
        this.username = user.getUserName();
        this.favouriteRecipes = new ArrayList<>();
        for (FavouriteRecipe favourite : user.getFavourites()) {
            favouriteRecipes.add(new FavouriteRecipeDTO(favourite));
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<FavouriteRecipeDTO> getFavouriteRecipes() {
        return favouriteRecipes;
    }

    public void setFavouriteRecipes(List<FavouriteRecipeDTO> favouriteRecipes) {
        this.favouriteRecipes = favouriteRecipes;
    }
}
