package session;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import utils.HttpUtils;

import java.util.HashMap;
import java.util.Map;

public class Search {
    // This map will hold all the required parameters needed for a spoonacular fetch.
    private Map<String, String> parameters = new HashMap<>();
    public Search() {}

    public void addParameter(String key, String value) {
        if(key.equals("titleMatch")) {
            value = value.replace(" ","%20");
        }
        parameters.put(key,value);
    }

    public static Search searchFromJsonObject(JsonObject object) {
        // Small workaround - due to the fact that titleMatch is called 'name' in your request from frontend.
        String name = object.get("name").getAsString();
        name = name.replace(" ", "%20");
        Search search = new Search();
        search.addParameter("titleMatch",name);

        // Take all parameters found, and if they have an equal in SearchParameterKey, add them as a parameter.
        SearchParameterKey[] validParameters = SearchParameterKey.values();
        for(int i = 0; i < validParameters.length; i++) {
            String validParamString = validParameters[i].toString();
            JsonElement element = object.get(validParamString);
            if(element != null) {
                search.addParameter(validParamString, element.getAsString());
            }
        }
        return search;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return HttpUtils.generateParameters(parameters);
    }
}
