package session;

public enum SearchParameterKey {
    // General Parameters for the fetch
    OFFSET("offset"),
    NUMBER("number"),
    AUTHOR("author"),
    TAGS("tags"),

    // Food Specific Parameters for the fetch
    TITLE_MATCH("titleMatch"),
    QUERY("query"),
    CUISINE("cuisine"),
    EXCLUDED_CUISINE("excludeCuisine"),
    DIET("diet"),
    INTOLERANCES("intolerances"),
    INCLUDE_INGREDIENTS("includeIngredients"),
    EXCLUDE_INGREDIENTS("excludeIngredients"),
    TYPE("type"),
    INSTRUCTIONS_REQUIRED("instructionsRequired"),
    FILL_INGREDIENTS("fillIngredients"),
    ADD_RECIPE_INFORMATION("addRecipeInformation"),
    ADD_RECIPE_NUTRITION("addRecipeNutrition"),
    MAX_READY_TIME("maxReadyTime"),
    
    // Equipment
    EQUIPMENT("equipment");

    // Dietary specific parameters for the fetch



    private String key;
    SearchParameterKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}
