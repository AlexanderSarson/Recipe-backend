/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

/**
 *
 * @author root
 */
public class FoodIngredientMeasureDTO {
    private MeasureDTO metric;
    private MeasureDTO us;

    public FoodIngredientMeasureDTO() {
    }

    public MeasureDTO getMetric() {
        return metric;
    }

    public void setMetric(MeasureDTO metric) {
        this.metric = metric;
    }

    public MeasureDTO getUs() {
        return us;
    }

    public void setUs(MeasureDTO us) {
        this.us = us;
    }


}
