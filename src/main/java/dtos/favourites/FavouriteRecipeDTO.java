package dtos.favourites;

import entity.FavouriteRecipe;

public class FavouriteRecipeDTO {
    private Long id;
    private String title;
    private String imgUrl;
    private Integer readyInMinutes;
    private Integer servings;

    public FavouriteRecipeDTO (Long id, String title, String imgUrl, Integer readyInMinutes, Integer servings) {
        this.id = id;
        this.title = title;
        this.imgUrl = imgUrl;
        this.readyInMinutes = readyInMinutes;
        this.servings = servings;
    }

    public FavouriteRecipeDTO (FavouriteRecipe favouriteRecipe) {
        this.id = favouriteRecipe.getId();
        this.title = favouriteRecipe.getTitle();
        this.imgUrl = favouriteRecipe.getImgUrl();
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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
