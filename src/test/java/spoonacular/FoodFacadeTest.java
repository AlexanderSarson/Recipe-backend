package spoonacular;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.RecipeDTO;
import dtos.RecipeDTOList;
import dtos.InstructionsDTO;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import session.Search;

import static org.junit.jupiter.api.Assertions.*;

public class FoodFacadeTest {

    private static final FoodFacade FOODFACADE = new FoodFacade();

    @Test
    void test_searchByName() {
        Search search = new Search();
        String title = "Falafel Burgers with Feta Cucumber Sauce";
        search.addParameter("titleMatch",title);
        search.addParameter("offset","0");
        search.addParameter("number","1");

        RecipeDTOList results = FOODFACADE.complexSearch(search);
        assertEquals(1, results.getResults().size());
        RecipeDTO result = results.getResults().get(0);

        assertEquals("Falafel Burgers with Feta Cucumber Sauce", result.getTitle());
        assertEquals(492564, result.getId());
    }
    
    @Test
    public void test_getRecipeById() {
        long expected = 324694  ;//716429;
        RecipeDTO recipe = FOODFACADE.getRecipeById(expected);
        long result = recipe.getId();
        assertEquals(expected, result);
    }

    @Test
    public void test_getInstructionsByRecipeId(){
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
        List<String> listOfExpectedProperties = new ArrayList<>();
        listOfExpectedProperties.add("id");
        listOfExpectedProperties.add("title");
        listOfExpectedProperties.add("extendedIngredients");
        JsonObject jsonObject = new JsonParser().parse(RandomResponseRecipes.RANDOM_RESPONSE).getAsJsonObject();
        JsonObject singleRecipe = jsonObject.get("recipes").getAsJsonArray().get(0).getAsJsonObject();
        RecipeDTO resultDTO = FOODFACADE.recipeParser(listOfExpectedProperties, singleRecipe);
        Long expectedId = 633344L;
        String expectedTitle = "Bacon Wrapped Pork Tenderloin";

        assertEquals(expectedId, (Long) resultDTO.getId());
        assertEquals(expectedTitle, resultDTO.getTitle());
    }
}
