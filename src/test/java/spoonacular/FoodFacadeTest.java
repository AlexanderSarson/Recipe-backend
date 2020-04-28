package spoonacular;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.FoodResultDTO;
import dtos.FoodResultDTOList;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FoodFacadeTest {

    private FoodFacade foodFacade = new FoodFacade();

    
    @Test
    void test_searchByName() {
        String search = "Falafel Burgers with Feta Cucumber Sauce";
        FoodResultDTOList results = foodFacade.searchByName(search, 1);
        assertEquals(1, results.getResults().size());
        FoodResultDTO result = results.getResults().get(0);

        assertEquals("Falafel Burgers with Feta Cucumber Sauce", result.getTitle());
        assertEquals(492564, result.getId());
    }

    
    @Test
    public void test_random() {
        int expectedRecipes = 5;
        FoodResultDTOList results = foodFacade.getRandomRecipes(expectedRecipes);
        assertEquals(expectedRecipes, results.getResults().size());
    }

    @Test
    public void test_recipeParser() {
        List<String> listOfExpectedProperties = List.of("id", "title", "extendedIngredients", "instructions");
        JsonObject jsonObject = new JsonParser().parse(RandomResponseRecipes.RANDOM_RESPONSE).getAsJsonObject();
        JsonObject singleRecipe = jsonObject.get("recipes").getAsJsonArray().get(0).getAsJsonObject();
        FoodResultDTO resultDTO = foodFacade.recipeParser(listOfExpectedProperties, singleRecipe);
        Long expectedId = 633344L;
        String expectedTitle = "Bacon Wrapped Pork Tenderloin";
        String expectedInstructions = "<ol><li>Remove about an inch off the tapered end of each tenderloin to make a perfect cylinder.</li><li>Season with salt, pepper, and a pinch of garlic powder.</li><li>Lay the bacon strips in a overlapping line on a sheet of cling wrap.  Place sage leaves all over bacon (about 9-10 leaves).</li><li>Place 1 piece of tenderloin across the short ends of the bacon and roll to cover with the bacon.</li><li>Repeat with the other tenderloin segments.</li><li>Preheat oven to 425 F.</li><li>Place the tenderloin in non-stick pan and sear on all sides over medium-high heat.</li><li>Transfer the pan to the preheated oven and cook for 8-10 minutes, turning the pieces after 5 minutes to ensure even cooking.</li></ol>";

        assertEquals(expectedId, (Long) resultDTO.getId());
        assertEquals(expectedTitle, resultDTO.getTitle());
        assertEquals(expectedInstructions, resultDTO.getInstructions());
    }
}
