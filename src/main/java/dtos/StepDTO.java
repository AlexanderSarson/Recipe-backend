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
public class StepDTO {
    @Expose(serialize = true, deserialize = true)
    private int number;
    @Expose(serialize = true, deserialize = true)
    private String step;

    public StepDTO() {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }
    
}
