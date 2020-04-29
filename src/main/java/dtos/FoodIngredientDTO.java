/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import java.util.List;

/**
 *
 * @author root
 */
public class FoodIngredientDTO {
    private long id;
    private FoodIngredientMeasureDTO measures;
    private String name;
    private List<String> meta;
    private String original;
    private String originalName;
    

    public FoodIngredientDTO() {
    }

    public FoodIngredientDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
