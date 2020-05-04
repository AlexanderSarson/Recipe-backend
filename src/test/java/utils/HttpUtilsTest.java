package utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class HttpUtilsTest {
    private final static String TEST_KEY = "ABCDEFG";
    private final static String TEST_URL = "https://api.chucknorris.io/jokes/random";
    HashMap<String, String> parameters;


    @BeforeEach
    public void setup() {
        parameters = new HashMap<>();
        parameters.put("param1", "first");
        parameters.put("param2", "second");
    }

    @Test
    public void test_fetch_without_parameters() {
        String data = HttpUtils.fetch(TEST_URL,HttpsMethod.GET,null,null);
        assertNotNull(data);
    }

    @Test
    public void test_fetch_with_parameters() {
        parameters.put("category", "animal");
        String data = HttpUtils.fetch(TEST_URL,HttpsMethod.GET,parameters,null);

        assertNotNull(data);
    }

    @Test
    public void test_generateParameters_with_valid_input() {
        String expected = "?param1=first&param2=second";
        String result = HttpUtils.generateParameters(parameters);

        assertEquals(expected,result);
    }

    @Test
    public void test_prepareUrl_without_parameters_and_key() {
        String expected = TEST_URL;
        String result = HttpUtils.prepareUrl(TEST_URL, null);

        assertEquals(expected,result);
    }

    @Test
    public void test_prepareUrl_with_parameters() {
        String expected = TEST_URL + "?param1=first&param2=second";
        String result = HttpUtils.prepareUrl(TEST_URL,parameters);
        assertEquals(expected,result);
    }
}