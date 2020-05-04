package utils;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

public class HttpUtils {
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
        String jsonResponse = null;
        try {
            // Prep the URL
            String completeUrl = prepareUrl(fetchUrl, parameters);

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
            // TODO(Benjamin): Logging on these exceptions?
            e.printStackTrace();
        }
        return jsonResponse;
    }

    public static String prepareUrl(String baseUrl, Map<String,String> parameters) {
        boolean hasParameters = false;
        String completeUrl = baseUrl;
        if(parameters != null) {
            hasParameters = true;
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
        for(String parameterKey: parameters.keySet()) {
            String value = parameters.get(parameterKey);
            parametersBuilder.append(parameterKey).append("=").append(value);
            if(count++ < parameters.size()-1) {
                parametersBuilder.append("&");
            }
        }
        return parametersBuilder.toString();
    }
}
