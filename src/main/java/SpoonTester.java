import dtos.FoodResultDTOList;
import spoon.FoodFacade;

import java.io.IOException;

public class SpoonTester {
    public static void main(String[] args) throws IOException {
        FoodFacade foodFacade = new FoodFacade();
        FoodResultDTOList dtoList = foodFacade.searchByName("burger",1);
        System.out.println(dtoList);
    }
}
