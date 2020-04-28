package spoonacular;

import dtos.FoodResultDTO;
import dtos.FoodResultDTOList;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FoodFacadeTest {
    private FoodFacade foodFacade = new FoodFacade();

    @Disabled
    @Test
    void test_searchByName() {
        String search = "Falafel Burgers with Feta Cucumber Sauce";
        FoodResultDTOList results = foodFacade.searchByName(search,1);
        assertEquals(1, results.getResults().size());
        FoodResultDTO result = results.getResults().get(0);

        assertEquals("Falafel Burgers with Feta Cucumber Sauce", result.getTitle());
        assertEquals(492564,result.getId());
    }
}