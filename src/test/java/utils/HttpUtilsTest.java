package utils;

import org.junit.jupiter.api.Test;
import spoon.FoodFacade;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HttpUtilsTest {
    private final static String TEST_URL = "https://api.chucknorris.io/jokes/random";

    @Test
    void test_fetch_without_parameters() {
        String data = HttpUtils.fetch(TEST_URL,HttpsMethod.GET,null,null);
        assertNotNull(data);
    }

    @Test
    void test_fetch_with_parameters() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("category", "animal");
        String data = HttpUtils.fetch(TEST_URL,HttpsMethod.GET,parameters,null);

        assertNotNull(data);
    }
    // NOTE(Benjamin): The case where we use the key, should be covered in the integration test of FoodFacade

    @Test
    void test_generateParameters_with_valid_input() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("param1", "first");
        parameters.put("param2", "second");
        String expected = "?param1=first&param2=second";
        String result = HttpUtils.generateParameters(parameters);

        assertEquals(expected,result);
    }
}