package dtos.favourites;

import com.fasterxml.jackson.annotation.JsonTypeName;
import entity.FavouriteRecipe;

@JsonTypeName("favourites")
public class FavouriteRecipeDTO {
    private Long id;
    private String title;
    private String image;
    private Integer readyInMinutes;
    private Integer servings;

    public FavouriteRecipeDTO() {
    }

    public FavouriteRecipeDTO (Long id, String title, String image, Integer readyInMinutes, Integer servings) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.readyInMinutes = readyInMinutes;
        this.servings = servings;
    }

    public FavouriteRecipeDTO (FavouriteRecipe favouriteRecipe) {
        this.id = favouriteRecipe.getId();
        this.title = favouriteRecipe.getTitle();
        this.image = favouriteRecipe.getImgUrl();
        this.readyInMinutes = favouriteRecipe.getReadyInMinutes();
        this.servings = favouriteRecipe.getServings();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(Integer readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }
}
