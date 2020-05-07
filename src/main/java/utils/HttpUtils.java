package utils;

import session.Search;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpUtils {
    private static final Logger logger = Logger.getLogger(HttpUtils.class.getName());

    private HttpUtils() {}
    /**
     * Fetches data from the given URL and returns the result as a string.
     * @param fetchUrl The URL to fetch from
     * @param method The HTTP method used for the request.
     * @param parameters The parameters of the request.
     * @param apiKey the API KEY use for authorization
     * @return A string with the result of the fetch, NULL if the fetch didn't get a response.
     */
    public static String fetch(String fetchUrl, HttpsMethod method,
                               Map<String,String> parameters, String apiKey) {
        String completeUrl = prepareUrl(fetchUrl, parameters);
        return doFetch(completeUrl,method,apiKey);
    }

    public static String complexFetch(String fetchUrl, HttpsMethod method, Search search, String apiKey) {
        return doFetch(fetchUrl + search.toString(), method, apiKey);
    }

    public static String doFetch(String completeUrl, HttpsMethod method, String apiKey) {
        String jsonResponse = null;
        try {
            // Setup URL connection
            URL url = new URL(completeUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            // Prep the Headers
            connection.setRequestMethod(method.name());
            connection.addRequestProperty("X-RapidAPI-Host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com");
            connection.addRequestProperty("X-RapidAPI-Key", apiKey);
            generateDefaultHeaders(connection);

            // Get the response
            Scanner scanner = new Scanner(connection.getInputStream());
            if (scanner.hasNext()) {
                jsonResponse = scanner.nextLine();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE,e.getMessage());
        }
        return jsonResponse;
    }



    public static String prepareUrl(String baseUrl, Map<String,String> parameters) {
        String completeUrl = baseUrl;
        if(parameters != null) {
            completeUrl += generateParameters(parameters);
        }
        return completeUrl;
    }

    /**
     * This will simply generate the default headers used in most operations.
     * @param connection The active connection, on which to set the headers.
     */
    private static void generateDefaultHeaders(HttpsURLConnection connection) {
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("User-agent", "server");
    }

    /**
     * Generates a String of parameters. This assumes that the ? has not been placed yet.
     * @param parameters The parameters to be converted.
     * @return A String with all the parameters, separated by '&' and starting with '?'
     */
    public static String generateParameters(Map<String, String> parameters) {
        StringBuilder parametersBuilder = new StringBuilder();
        parametersBuilder.append("?");
        int count = 0;
        for (Map.Entry<String,String> entry : parameters.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            parametersBuilder.append(key).append("=").append(value);
            if(count++ < parameters.size()-1) {
                parametersBuilder.append("&");
            }
        }
        return parametersBuilder.toString();
    }
}
