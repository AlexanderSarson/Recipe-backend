package dtos.favourites;

import java.util.ArrayList;
import java.util.List;

public class FavouriteRecipeDtoList {

    private List<FavouriteRecipeDTO> favouriteRecipes;

    public FavouriteRecipeDtoList() {
        this.favouriteRecipes = new ArrayList<>();
    }

    public List<FavouriteRecipeDTO> getFavouriteRecipes() {
        return favouriteRecipes;
    }

    public void addRecipeToList (FavouriteRecipeDTO recipe) {
        this.favouriteRecipes.add(recipe);
    }

    public int getSize() {
        return this.favouriteRecipes.size();
    }
}
