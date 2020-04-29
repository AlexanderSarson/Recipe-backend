package dtos;

import java.util.ArrayList;
import java.util.List;

public class FoodResultDTOList {
    private List<FoodResultDTO> results;

    public FoodResultDTOList() {
        this.results = new ArrayList<>();
    }

    public FoodResultDTOList(List<FoodResultDTO> results) {
        this.results = results;
    }

    public List<FoodResultDTO> getResults() {
        return results;
    }

    public void setResults(List<FoodResultDTO> results) {
        this.results = results;
    }
    
    public void addFoodResult(FoodResultDTO dto){
        this.results.add(dto);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(FoodResultDTO dto: results) {
            stringBuilder.append(dto.toString());
        }
        return stringBuilder.toString();
    }
}
