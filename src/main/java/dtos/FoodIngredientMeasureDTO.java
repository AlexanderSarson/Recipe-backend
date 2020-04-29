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
    private MeasureMetricDTO metric;
    private MeasureUsDTO us;

    public FoodIngredientMeasureDTO() {
    }

    public MeasureMetricDTO getMetric() {
        return metric;
    }

    public void setMetric(MeasureMetricDTO metric) {
        this.metric = metric;
    }

    public MeasureUsDTO getUs() {
        return us;
    }

    public void setUs(MeasureUsDTO us) {
        this.us = us;
    }

}
