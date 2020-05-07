package spoonacular;

import com.google.gson.*;
import dtos.FoodIngredientDTO;
import dtos.InstructionsDTO;
import dtos.RecipeDTO;
import dtos.RecipeDTOList;
import session.Search;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static utils.HttpUtils.complexFetch;
import static utils.HttpUtils.fetch;
import static utils.HttpsMethod.GET;

public class FoodFacade {

    private static final String KEY_PROPERTIES = "key.properties";
    public static final String API_KEY = "apiKey";
    private String apiKey;
    private static final String BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com";
    private static final String SEARCH_URL = BASE_URL + "/recipes/complexSearch";
    private static final String RANDOM_URL = BASE_URL + "/recipes/random";
    private static final String AUTOCOMPLETE_INGREDIENT = BASE_URL + "/food/ingredients/autocomplete";
    private static final List<String> LIST_OF_FOOD_PROPERTIES_RANDOM = Arrays.asList("id", "title", "servings", "readyInMinutes", "summary", "image", "dairyFree", "glutenFree", "vegan", "vegetarian", "veryHealthy");
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
                apiKey = keyProperties.getProperty(API_KEY);
            } else {
                apiKey = System.getenv(API_KEY);
            }

        } catch (IOException ignored) {
            // If it is not present try to find it as a system variable.
            apiKey = System.getenv(API_KEY);
        }
    }

    /**
     * Gets a RecipeDTOList with recipes matching the complex search
     * @param search the complex search object.
     * @return A RecipeDTOList containing a list of all the FoodResultDTOs
     */
    public RecipeDTOList complexSearch(Search search) {
        String data = complexFetch(SEARCH_URL,GET,search,apiKey);
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
        return new ArrayList<>(Arrays.asList(instructionsArray));
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
     * Tries to find matching ingredients base on the partial match
     * @param partialMatch the match, with which to find ingredients
     * @param number the number of returned results.
     * @return A List of FoodIngredientDTOs.
     */
    public List<FoodIngredientDTO> autoCompleteIngredient(String partialMatch, int number) {
        Map<String,String> parameters = new HashMap<>();
        String sanitizedInput = sanitizeString(partialMatch);
        parameters.put("query", sanitizedInput);
        parameters.put("number", ""+number);
        String data = fetch(AUTOCOMPLETE_INGREDIENT, GET, parameters, apiKey);
        JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();
        return ingredientsParser(jsonArray);
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
        handleGeneralResults(jsonObject, resultDTOList);
        List<RecipeDTO> foodResultList = new ArrayList<>();
        if (jsonObject.get("recipes") != null) {
            jsonObject.get("recipes").getAsJsonArray().forEach(obj -> foodResultList.add(recipeParser(listOfProperties, obj.getAsJsonObject())) );
        } else if (jsonObject.get("results") != null) {
            jsonObject.get("results").getAsJsonArray().forEach(obj -> foodResultList.add(recipeParser(listOfProperties, obj.getAsJsonObject())) );
        } else {
            foodResultList.add(recipeParser(listOfProperties, jsonObject));
        }
        resultDTOList.setResults(foodResultList);
        return resultDTOList;
    }

    private void handleGeneralResults(JsonObject jsonObject, RecipeDTOList resultDTOList) {
        JsonElement offset = jsonObject.get("offset");
        if(offset != null)
            resultDTOList.setOffset(offset.getAsInt());

        JsonElement number = jsonObject.get("number");
        if(number != null)
            resultDTOList.setNumber(number.getAsInt());

        JsonElement totalResults = jsonObject.get("totalResults");
        if(totalResults != null)
            resultDTOList.setTotalResults(totalResults.getAsInt());
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
            JsonElement id = ingredient.getAsJsonObject().get("id");
            if(id != null)
                foodIngredient.setId(id.getAsLong());
            JsonElement name = ingredient.getAsJsonObject().get("name");
            if(name != null)
                foodIngredient.setName(name.getAsString());
            ingredients.add(foodIngredient);
        });
        return ingredients;
    }

    public String sanitizeString(String input) {
        StringBuilder stringBuilder = new StringBuilder();
        Pattern pattern = Pattern.compile("[a-zA-Z ]+");
        Matcher matcher = pattern.matcher(input);
        while(matcher.find()) {
            stringBuilder.append(matcher.group());
        }
        return stringBuilder.toString();
    }
}
