package spoonacular;

import com.google.gson.Gson;
import dtos.FoodResultDTOList;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static utils.HttpUtils.fetch;
import static utils.HttpsMethod.*;

public class FoodFacade {
    private static final String KEY_PROPERTIES = "key.properties";
    private String apiKey;
    private final String BASE_URL = "https://api.spoonacular.com";
    private final String SEARCH_URL = BASE_URL + "/recipes/complexSearch";
    private Gson gson = new Gson();

    public FoodFacade() {
        Properties keyProperties = new Properties();
        try {
            keyProperties.load(FoodFacade.class.getClassLoader().getResourceAsStream(KEY_PROPERTIES));
            apiKey = keyProperties.getProperty("apiKey");
        } catch (IOException ignored) {
        }
    }

    /**
     * Gets a FoodResultDTOList with recipes matching the searched name
     * @param name The name of the recipe to search for
     * @param amount the number of items to be returned.
     * @return A FoodResultDTOList containing a list of all the FoodResultDTOs
     */
    public FoodResultDTOList searchByName(String name, int amount) {
        Map<String, String> parameters = new HashMap<>();
        String titleMatch = name.replaceAll(" ", "%20");
        parameters.put("titleMatch", titleMatch);
        parameters.put("number", ""+amount);
        String data = fetch(SEARCH_URL, GET, parameters, apiKey);
        return gson.fromJson(data, FoodResultDTOList.class);
    }



}
