package spoonacular;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.FoodIngredientDTO;
import dtos.RecipeDTO;
import dtos.RecipeDTOList;
import dtos.InstructionsDTO;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static utils.HttpUtils.fetch;
import static utils.HttpsMethod.*;

public class FoodFacade {

    private static final String KEY_PROPERTIES = "key.properties";
    private String apiKey;
    private static final String BASE_URL = "https://api.spoonacular.com";
    private static final String SEARCH_URL = BASE_URL + "/recipes/complexSearch";
    private static final String RANDOM_URL = BASE_URL + "/recipes/random";
    private static final List<String> LIST_OF_FOOD_PROPERTIES_DETAILED = Arrays.asList("id", "title", "servings", "readyInMinutes", "cuisines", "dairyFree", "glutenFree", "vegan", "vegetarian", "veryHealthy", "dishTypes", "extendedIngredients", "summary");
    private static final List<String> LIST_OF_FOOD_PROPERTIES_RANDOM = Arrays.asList("id", "title", "servings", "readyInMinutes", "summary");
        private static final List<String> LIST_OF_FOOD_PROPERTIES_SEARCH = Arrays.asList("id", "title", "image");

    private Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public FoodFacade() {
        Properties keyProperties = new Properties();
        try {
            // try to get the key from file
            InputStream inputStream = FoodFacade.class.getClassLoader().getResourceAsStream(KEY_PROPERTIES);
            if (inputStream != null) {
                keyProperties.load(inputStream);
                apiKey = keyProperties.getProperty("apiKey");
            } else {
                apiKey = System.getenv("apiKey");
            }

        } catch (IOException ignored) {
            // If it is not present try to find it as a system variable.
            apiKey = System.getenv("apiKey");
        }
    }

    /**
     * Gets a RecipeDTOList with recipes matching the searched name
     *
     * @param name The name of the recipe to search for
     * @param numberOfRecipes the number of items to be returned.
     * @return A RecipeDTOList containing a list of all the FoodResultDTOs
     */
    public RecipeDTOList searchByName(String name, int numberOfRecipes) {
        Map<String, String> parameters = new HashMap<>();
        String titleMatch = name.replaceAll(" ", "%20");
        parameters.put("titleMatch", titleMatch);
        parameters.put("number", "" + numberOfRecipes);
        String data = fetch(SEARCH_URL, GET, parameters, apiKey);
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        return getFoodResultDTOList(LIST_OF_FOOD_PROPERTIES_SEARCH, jsonObject);
    }

    /**
     * Gets a detailed recipe by id with instructions
     *
     * @param recipeId
     * @return a detailed recipe with instructions
     */
    public RecipeDTO getRecipeById(long recipeId) {
        RecipeDTOList resultDTOList = new RecipeDTOList();
        String url = BASE_URL + "/recipes/" + recipeId + "/information";
        String data = fetch(url, GET, null, apiKey);
        RecipeDTO recipe = gson.fromJson(data, RecipeDTO.class);
        recipe.setInstructions(getInstructionsByRecipeId(recipeId));
        return recipe;
    }

    /**
     * GETS a lists of instructions
     *
     * @param recipeId
     * @return a list of instructions to the recipe
     */
    public List<InstructionsDTO> getInstructionsByRecipeId(long recipeId) {
        String url = BASE_URL + "/recipes/" + recipeId + "/analyzedInstructions";
        String data = fetch(url, GET, null, apiKey);
        InstructionsDTO[] instructionsArray = gson.fromJson(data, InstructionsDTO[].class);
        List<InstructionsDTO> instructionsList = new ArrayList<>(Arrays.asList(instructionsArray));
        return instructionsList;
    }

    /**
     * Gets a RecipeDTOList with random recipes
     *
     * @param numberOfRecipes the number of items to be returned.
     * @return A RecipeDTOList containing a list of the random FoodResultDTOs
     */
    public RecipeDTOList getRandomRecipes(int numberOfRecipes) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("number", "" + numberOfRecipes);
        String data = fetch(RANDOM_URL, GET, parameters, apiKey);
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        return getFoodResultDTOList(LIST_OF_FOOD_PROPERTIES_RANDOM, jsonObject);
    }

    /**
     * Gets a RecipeDTOList with specific properties
     *
     * @param listOfProperties the properties to extract
     * @param jsonObject the response object from endpoint
     * @return RecipeDTOList containing a list of FoodResultDTOs
     */
    public RecipeDTOList getFoodResultDTOList(List<String> listOfProperties, JsonObject jsonObject) {
        RecipeDTOList resultDTOList = new RecipeDTOList();
        List<RecipeDTO> foodResultList = new ArrayList<>();
        if (jsonObject.get("recipes") != null) {
            jsonObject.get("recipes").getAsJsonArray().forEach(obj -> {
                foodResultList.add(recipeParser(listOfProperties, obj.getAsJsonObject()));
            });
        } else if (jsonObject.get("results") != null) {
            jsonObject.get("results").getAsJsonArray().forEach(obj -> {
                foodResultList.add(recipeParser(listOfProperties, obj.getAsJsonObject()));
            });
        } else {
            foodResultList.add(recipeParser(listOfProperties, jsonObject));
        }
        resultDTOList.setResults(foodResultList);
        return resultDTOList;
    }

    /**
     * Gets a RecipeDTO from parsing
     *
     * @param listOfProperties the properties to extract
     * @param jsonObject the response object from endpoint
     * @return A RecipeDTO containing a recipe
     */
    public RecipeDTO recipeParser(List<String> listOfProperties, JsonObject jsonObject) {
        RecipeDTO foodDTO = new RecipeDTO();
        listOfProperties.forEach(prop
                -> {
            if (prop.equals("extendedIngredients")) {
                foodDTO.setField(prop, ingredientsParser(jsonObject.get("extendedIngredients").getAsJsonArray()));
            } else {
                foodDTO.setField(prop, jsonObject.get(prop));
            }
        });
        return foodDTO;
    }

    /**
     * Parses a JsonArray containing ingredients
     *
     * @param jsonIngredients a JsonArray containing ingredients
     * @return A List of FoodIngredientDTOs containing ingredients
     */
    public List<FoodIngredientDTO> ingredientsParser(JsonArray jsonIngredients) {
        List<FoodIngredientDTO> ingredients = new ArrayList<>();
        jsonIngredients.forEach(ingredient -> {
            FoodIngredientDTO foodIngredient = new FoodIngredientDTO();
            foodIngredient.setId(ingredient.getAsJsonObject().get("id").getAsLong());
            foodIngredient.setName(ingredient.getAsJsonObject().get("name").getAsString());
            ingredients.add(foodIngredient);
        });
        return ingredients;
    }

}
