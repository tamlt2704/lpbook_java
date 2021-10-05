package com.jlpbook.app.solver.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LpModel {
    List<Variable> variables = new ArrayList<>();
    List<Constraint> constraints = new ArrayList<>();
    ObjectiveFunction objectiveFunction;

    public void addVariables(List<Variable> variables) {
        this.variables.addAll(variables);
    }

    public void addConstraints(List<Constraint> populateConstraints) {
        constraints.addAll(populateConstraints);
    }
}
