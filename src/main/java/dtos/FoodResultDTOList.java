package dtos;

import java.util.List;

public class FoodResultDTOList {
    private List<FoodResultDTO> results;

    public FoodResultDTOList(List<FoodResultDTO> results) {
        this.results = results;
    }

    public List<FoodResultDTO> getResults() {
        return results;
    }

    public void setResults(List<FoodResultDTO> results) {
        this.results = results;
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
