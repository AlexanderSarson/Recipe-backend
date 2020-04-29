package dtos;

import java.util.List;

public class FoodResultWithInstructionsDTO {

    private long id;
    private String title;
    private String image;
    private List<String> imageUrls;
    private int servings;
    private int readyInMinutes;
    private List<String> cuisines;
    private boolean dairyFree;
    private boolean glutenFree;
    private boolean vegan;
    private boolean vegetarian;
    private boolean veryHealthy;
    private List<String> dishTypes;
    private List<FoodIngredientDTO> extendedIngredients;
    private String summary;
    private List<InstructionsDTO> instructions;

    public FoodResultWithInstructionsDTO() {
    }

    public FoodResultWithInstructionsDTO(FoodResultDTO recipe, List<InstructionsDTO> instructions) {
        this.id = recipe.getId();
        this.title = recipe.getTitle();
        this.image = recipe.getImage();
        this.imageUrls = recipe.getImageUrls();
        this.servings = recipe.getServings();
        this.readyInMinutes = recipe.getReadyInMinutes();
        this.cuisines = recipe.getCuisines();
        this.dairyFree = recipe.isDairyFree();
        this.glutenFree = recipe.isGlutenFree();
        this.vegan = recipe.isVegan();
        this.vegetarian = recipe.isVegetarian();
        this.veryHealthy = recipe.isVeryHealthy();
        this.dishTypes = recipe.getDishTypes();
        this.extendedIngredients = recipe.getExtendedIngredients();
        this.summary = recipe.getSummary();
        this.instructions = instructions;
    }
    
    public FoodResultWithInstructionsDTO(long id, String image, List<String> imageUrls, int readyInMinutes, int servings, String title) {
        this.id = id;
        this.image = image;
        this.imageUrls = imageUrls;
        this.readyInMinutes = readyInMinutes;
        this.servings = servings;
        this.title = title;
    }
    
    public FoodResultWithInstructionsDTO(long id, String image, List<String> imageUrls, int readyInMinutes, int servings, String title, List<FoodIngredientDTO> ingredients) {
        this.id = id;
        this.image = image;
        this.imageUrls = imageUrls;
        this.readyInMinutes = readyInMinutes;
        this.servings = servings;
        this.title = title;
        this.extendedIngredients = ingredients;
    }
    

    public List<FoodIngredientDTO> getExtendedIngredients() {
        return extendedIngredients;
    }

    public void setExtendedIngredients(List<FoodIngredientDTO> ingredients) {
        this.extendedIngredients = ingredients;
    }

    public List<String> getCuisines() {
        return cuisines;
    }

    public void setCuisines(List<String> cuisines) {
        this.cuisines = cuisines;
    }

    public List<InstructionsDTO> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<InstructionsDTO> instructions) {
        this.instructions = instructions;
    }
    
    public boolean isDairyFree() {
        return dairyFree;
    }

    public void setDairyFree(boolean dairyFree) {
        this.dairyFree = dairyFree;
    }

    public boolean isGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

    public boolean isVegan() {
        return vegan;
    }

    public void setVegan(boolean vegan) {
        this.vegan = vegan;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public boolean isVeryHealthy() {
        return veryHealthy;
    }

    public void setVeryHealthy(boolean veryHealthy) {
        this.veryHealthy = veryHealthy;
    }

    public List<String> getDishTypes() {
        return dishTypes;
    }

    public void setDishTypes(List<String> dishTypes) {
        this.dishTypes = dishTypes;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(int readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "FetchResultDTO{"
                + "id=" + id
                + ", image='" + image + '\''
                + ", imageUrls=" + imageUrls
                + ", readyInMinutes=" + readyInMinutes
                + ", servings=" + servings
                + ", title='" + title + '\''
                + '}';
    }
}
