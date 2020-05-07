package spoonacular;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.FoodIngredientDTO;
import dtos.RecipeDTO;
import dtos.RecipeDTOList;
import dtos.InstructionsDTO;
import java.util.List;
import org.junit.jupiter.api.Test;
import session.Search;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;

public class FoodFacadeTest {

    private static final FoodFacade FOODFACADE = new FoodFacade();

    
    @Test
    void test_searchByName() {
        Search search = new Search();
        String title = "Falafel Burgers with Feta Cucumber Sauce";
        search.addParameter("titleMatch", title);
        search.addParameter("offset", "0");
        search.addParameter("number", "1");

        RecipeDTOList results = FOODFACADE.complexSearch(search);
        assertEquals(1, results.getResults().size());
        RecipeDTO result = results.getResults().get(0);

        assertEquals("Falafel Burgers with Feta Cucumber Sauce", result.getTitle());
        assertEquals(492564, result.getId());
    }

    
    @Test
    public void test_getRecipeById() {
        long expected = 324694;//716429;
        RecipeDTO recipe = FOODFACADE.getRecipeById(expected);
        long result = recipe.getId();
        assertEquals(expected, result);
    }

    
    @Test
    public void test_getInstructionsByRecipeId() {
        long recipeId = 324694;
        int expectedNumberOfInstructions = 2;
        List<InstructionsDTO> result = FOODFACADE.getInstructionsByRecipeId(recipeId);
        assertEquals(expectedNumberOfInstructions, result.size());
    }

    @Test
    public void test_random() {
        int expectedRecipes = 5;
        RecipeDTOList results = FOODFACADE.getRandomRecipes(expectedRecipes);
        assertEquals(expectedRecipes, results.getResults().size());
    }

    
    @Test
    public void test_recipeParser() {
        List<String> listOfExpectedProperties = List.of("id", "title", "extendedIngredients");
        JsonObject jsonObject = new JsonParser().parse(RandomResponseRecipes.RANDOM_RESPONSE).getAsJsonObject();
        JsonObject singleRecipe = jsonObject.get("recipes").getAsJsonArray().get(0).getAsJsonObject();
        RecipeDTO resultDTO = FOODFACADE.recipeParser(listOfExpectedProperties, singleRecipe);
        Long expectedId = 633344L;
        String expectedTitle = "Bacon Wrapped Pork Tenderloin";

        assertEquals(expectedId, (Long) resultDTO.getId());
        assertEquals(expectedTitle, resultDTO.getTitle());
    }

    
    @Test
    public void test_autoCompleteIngredient_with_some_returned_results() {
        int numberOfResults = 5;
        String partialMatch = "app";
        List<String> listOfExpectedNames = List.of("apple", "applesauce", "apple juice", "apple cider", "apple jelly");
        List<FoodIngredientDTO> ingredients = FOODFACADE.autoCompleteIngredient(partialMatch, numberOfResults);
        ingredients.forEach(ingredient -> {
            assertTrue(listOfExpectedNames.contains(ingredient.getName()));
        });
    }

    
    @Test
    public void test_autoCompleteIngredient_without_returned_results() {
        int numberOfResults = 5;
        String partialMatch = "askdklajdjakldjakljdkljalkdjklajd"; // This should not get a match
        List<FoodIngredientDTO> ingredients = FOODFACADE.autoCompleteIngredient(partialMatch, numberOfResults);
        assertTrue(ingredients.isEmpty());
    }

    @Test
    public void test_sanitizeString_with_a_safe_string() {
        String input = "apple juice";
        String output = FOODFACADE.sanitizeString(input);
        assertEquals(input,output);
    }

    @Test
    public void test_sanitizeString_with_an_unsafe_string() {
        String input = "apple?--=!0768¤¤#¤()/#sauce";
        String expected = "applesauce";
        String result = FOODFACADE.sanitizeString(input);
        assertEquals(expected,result);
    }
}
