/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import com.google.gson.annotations.Expose;
import java.util.List;

/**
 *
 * @author root
 */
public class FoodIngredientDTO {
    @Expose(serialize = true, deserialize = true)
    private long id;
    @Expose(serialize = true, deserialize = true)
    private FoodIngredientMeasureDTO measures;
    @Expose(serialize = true, deserialize = true)
    private String name;
    @Expose(serialize = true, deserialize = true)
    private List<String> meta;
    @Expose(serialize = true, deserialize = true)
    private String original;
    @Expose(serialize = true, deserialize = true)
    private String originalName;
    

    public FoodIngredientDTO() {
    }

    public FoodIngredientDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public FoodIngredientMeasureDTO getMeasures() {
        return measures;
    }

    public void setMeasures(FoodIngredientMeasureDTO measures) {
        this.measures = measures;
    }

    public List<String> getMeta() {
        return meta;
    }

    public void setMeta(List<String> meta) {
        this.meta = meta;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
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
