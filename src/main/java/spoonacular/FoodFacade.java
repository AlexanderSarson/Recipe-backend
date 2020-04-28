package spoonacular;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.FoodIngredientDTO;
import dtos.FoodResultDTO;
import dtos.FoodResultDTOList;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import net.minidev.json.parser.JSONParser;

import static utils.HttpUtils.fetch;
import static utils.HttpsMethod.*;

public class FoodFacade {

    private static final String KEY_PROPERTIES = "key.properties";
    private String apiKey;
    private static final String BASE_URL = "https://api.spoonacular.com";
    private static final String SEARCH_URL = BASE_URL + "/recipes/complexSearch";
    private static final String RANDOM_URL = BASE_URL + "/recipes/random";
    private static final List<String> LIST_OF_FOOD_PROPERTIES = List.of("id", "title", "extendedIngredients", "instructions");

    private Gson gson = new Gson();

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
     * Gets a FoodResultDTOList with recipes matching the searched name
     *
     * @param name The name of the recipe to search for
     * @param numberOfRecipes the number of items to be returned.
     * @return A FoodResultDTOList containing a list of all the FoodResultDTOs
     */
    public FoodResultDTOList searchByName(String name, int numberOfRecipes) {
        Map<String, String> parameters = new HashMap<>();
        String titleMatch = name.replaceAll(" ", "%20");
        parameters.put("titleMatch", titleMatch);
        parameters.put("number", "" + numberOfRecipes);
        String data = fetch(SEARCH_URL, GET, parameters, apiKey);
        return gson.fromJson(data, FoodResultDTOList.class);
    }

    /**
     * Gets a FoodResultDTOList with random recipes
     *
     * @param numberOfRecipes the number of items to be returned.
     * @return A FoodResultDTOList containing a list of the random FoodResultDTOs
     */
    public FoodResultDTOList getRandomRecipes(int numberOfRecipes) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("number", "" + numberOfRecipes);
        String data = fetch(RANDOM_URL, GET, parameters, apiKey);
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        return getFoodResultDTOList(LIST_OF_FOOD_PROPERTIES, jsonObject);
    }
    
    public FoodResultDTOList getFoodResultDTOList(List<String> listOfProperties, JsonObject jsonObject){
        FoodResultDTOList resultDTOList = new FoodResultDTOList();
        List<FoodResultDTO> foodResultList = new ArrayList<>();
        if(jsonObject.get("recipes") != null){
            jsonObject.get("recipes").getAsJsonArray().forEach(obj -> {
                foodResultList.add(recipeParser(listOfProperties, obj.getAsJsonObject()));
            });
        } else if (jsonObject.get("results") != null){
            jsonObject.get("results").getAsJsonArray().forEach(obj -> {
                foodResultList.add(recipeParser(listOfProperties, obj.getAsJsonObject()));
            });
        }
        resultDTOList.setResults(foodResultList);
        return resultDTOList;
    }

    public FoodResultDTO recipeParser(List<String> listOfProperties, JsonObject jsonObject) {
        FoodResultDTO foodDTO = new FoodResultDTO();
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

    public List<FoodIngredientDTO> ingredientsParser(JsonArray jsonIngredients){
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
