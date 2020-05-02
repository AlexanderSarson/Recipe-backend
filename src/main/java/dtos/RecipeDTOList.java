package dtos;

import java.util.ArrayList;
import java.util.List;

public class RecipeDTOList {
    private List<RecipeDTO> results;
    private int offset;
    private int number;
    private int totalResults;

    public RecipeDTOList() {
        this.results = new ArrayList<>();
    }

    public RecipeDTOList(List<RecipeDTO> results) {
        this.results = results;
    }

    public RecipeDTOList(List<RecipeDTO> results, int offset, int number, int totalResults) {
        this.results = results;
        this.offset = offset;
        this.number = number;
        this.totalResults = totalResults;
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

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
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
