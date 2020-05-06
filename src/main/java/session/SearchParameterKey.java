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
    INSTRUCTIONS_REQUIRED("instructionsRequired"),              // boolean
    FILL_INGREDIENTS("fillIngredients"),                        // boolean
    ADD_RECIPE_INFORMATION("addRecipeInformation"),             // boolean
    ADD_RECIPE_NUTRITION("addRecipeNutrition"),                 // boolean
    MAX_READY_TIME("maxReadyTime"),                             // int
    IGNORE_PANTRY("ignorePantry"),                              // boolean
    SORT("sort"),
    SORT_DIRECTION("sortDirection"),

    // Dietary specific parameters for the fetch
    // MIN >= 0
    // MAX <= 100
    MIN_CARBS("minCarbs"),
    MAX_CARBS("maxCarbs"),
    MIN_PROTEIN("minProtein"),
    MAX_PROTEIN("maxProtein"),
    MIN_CALORIES("minCalories"),
    MAX_CALORIES("maxCalories"),
    MIN_FAT("minFat"),
    MAX_FAT("maxFat"),
    MIN_ALCOHOL("minAlcohol"),
    MAX_ALCOHOL("maxAlcohol"),
    MIN_CAFFEINE("minCaffeine"),
    MAX_CAFFEINE("maxCaffeine"),
    MIN_COPPER("minCopper"),
    MAX_COPPER("maxCopper"),
    MIN_CALCIUM("minCalcium"),
    MAX_CALCIUM("maxCalcium"),
    MIN_CHOLINE("minCholine"),
    MAX_CHOLINE("maxCholine"),
    MIN_CHOLESTEROL("minCholesterol"),
    MAX_CHOLESTEROL("maxCholesterol"),
    MIN_FLUORIDE("minFluoride"),
    MAX_FLUORIDE("maxFluoride"),
    MIN_SATURATED_FAT("minSaturatedFat"),
    MAX_SATURATED_FAT("maxSaturatedFat"),
    MIN_VITAMIN_A("minVitaminA"),
    MAX_VITAMIN_A("maxVitaminA"),
    MIN_VITAMIN_C("minVitaminC"),
    MAX_VITAMIN_C("maxVitaminC"),
    MIN_VITAMIN_D("minVitaminD"),
    MAX_VITAMIN_D("maxVitaminD"),
    MIN_VITAMIN_E("minVitaminE"),
    MAX_VITAMIN_E("maxVitaminE"),
    MIN_VITAMIN_K("minVitaminK"),
    MAX_VITAMIN_K("maxVitaminK"),
    MIN_VITAMIN_B1("minVitaminB1"),
    MAX_VITAMIN_B1("maxVitaminB1"),
    MIN_VITAMIN_B2("minVitaminB2"),
    MAX_VITAMIN_B2("maxVitaminB2"),
    MIN_VITAMIN_B3("minVitaminB3"),
    MAX_VITAMIN_B3("maxVitaminB3"),
    MIN_VITAMIN_B5("minVitaminB5"),
    MAX_VITAMIN_B5("maxVitaminB5"),
    MIN_VITAMIN_B6("minVitaminB6"),
    MAX_VITAMIN_B6("maxVitaminB6"),
    MIN_VITAMIN_B12("minVitaminB12"),
    MAX_VITAMIN_B12("maxVitaminB12"),
    MIN_FIBER("minFiber"),
    MAX_FIBER("maxFiber"),
    MIN_FOLATE("minFolate"),
    MAX_FOLATE("maxFolate"),
    MIN_FOLIC_ACID("minFolicAcid"),
    MAX_FOLIC_ACID("maxFolicAcid"),
    MIN_IODINE("minIodine"),
    MAX_IODINE("maxIodine"),
    MIN_IRON("minIron"),
    MAX_IRON("maxIron"),
    MIN_MAGNESIUM("minMagnesium"),
    MAX_MAGNESIUM("maxMagnesium"),
    MIN_MANGANESE("minManganese"),
    MAX_MANGANESE("maxManganese"),
    MIN_PHOSPHORUS("minPhosphorus"),
    MAX_PHOSPHORUS("maxPhosphorus"),
    MIN_POTASSIUM("minPotassium"),
    MAX_POTASSIUM("maxPotassium"),
    MIN_SELENIUM("minSelenium"),
    MAX_SELENIUM("maxSelenium"),
    MIN_SODIUM("minSodium"),
    MAX_SODIUM("maxSodium"),
    MIN_SUGAR("minSugar"),
    MAX_SUGAR("maxSugar"),
    MIN_ZINC("minZinc"),
    MAX_ZINC("maxZinc"),

    // Equipment
    EQUIPMENT("equipment");

    private String key;
    SearchParameterKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}
