package session;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SearchTest {
    private String json = "{offset:2,name:\"falafel\",includeIngredients:\"salt,bacon,cheese\" }";
    private JsonObject object;

    @BeforeEach
    public void setup() {
        object = new JsonParser().parse(json).getAsJsonObject();
    }

    @Test
    public void testSearchFromJsonObject_with_valid_parameters() {
        Search search = Search.searchFromJsonObject(object);

        assertEquals("falafel",search.getParameters().get("titleMatch"));
        assertTrue(search.getParameters().containsKey("includeIngredients"));
        assertTrue(search.getParameters().containsValue("salt,bacon,cheese"));
    }

    @Test
    public void testSearchFromJsonObject_with_invalid_parameters() {
        String json = "{name:\"falafel\",includeDIngredients:\"salt,bacon,cheese\" }";
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

        Search search = Search.searchFromJsonObject(jsonObject);

        assertEquals("falafel",search.getParameters().get("titleMatch"));
        assertEquals(1,search.getParameters().size());
    }

    @Test
    public void testSearchParametersToString() {
        String expected = "?offset=2&titleMatch=falafel&includeIngredients=salt,bacon,cheese";
        Search search = Search.searchFromJsonObject(object);

        assertEquals(expected, search.toString());
    }
}