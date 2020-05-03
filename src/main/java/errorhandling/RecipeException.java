package errorhandling;

public class RecipeException extends Exception{
    public static final String RECIPE_NOT_FOUND = "Recipe not found";

    public RecipeException (String message) {
        super(message);
    }
}