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
public class MeasureUsDTO {
    private double amount;
    private String unitLong;
    private String unitShort;

    public MeasureUsDTO() {
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
