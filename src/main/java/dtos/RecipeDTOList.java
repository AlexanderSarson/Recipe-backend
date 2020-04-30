package dtos;

import java.util.ArrayList;
import java.util.List;

public class RecipeDTOList {
    private List<RecipeDTO> results;

    public RecipeDTOList() {
        this.results = new ArrayList<>();
    }

    public RecipeDTOList(List<RecipeDTO> results) {
        this.results = results;
    }

    public List<RecipeDTO> getResults() {
        return results;
    }

    public void setResults(List<RecipeDTO> results) {
        this.results = results;
    }
    
    public void addFoodResult(RecipeDTO dto){
        this.results.add(dto);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(RecipeDTO dto: results) {
            stringBuilder.append(dto.toString());
        }
        return stringBuilder.toString();
    }
}
