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
public class InstructionsDTO {
    private String name;
    private List<StepDTO> steps;

    public InstructionsDTO() {
    }

    public List<StepDTO> getSteps() {
        return steps;
    }

    public void setSteps(List<StepDTO> steps) {
        this.steps = steps;
    }

    
}
