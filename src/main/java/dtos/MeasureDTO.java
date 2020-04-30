/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import com.google.gson.annotations.Expose;

/**
 *
 * @author root
 */
public class MeasureDTO {
    @Expose(serialize = true, deserialize = true)
    private double amount;
    @Expose(serialize = true, deserialize = true)
    private String unitLong;
    @Expose(serialize = true, deserialize = true)
    private String unitShort;

    public MeasureDTO() {
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUnitLong() {
        return unitLong;
    }

    public void setUnitLong(String unitLong) {
        this.unitLong = unitLong;
    }

    public String getUnitShort() {
        return unitShort;
    }

    public void setUnitShort(String unitShort) {
        this.unitShort = unitShort;
    }
    
}
