package com.jlpbook.app.solver;

import com.jlpbook.app.solver.model.Variable;

import java.util.HashMap;
import java.util.Map;

public class VariableFactory {
    Map<Integer, Variable> variables = new HashMap<>();

    public VariableFactory(Map<Integer, Variable> variables) {
        this.variables = variables;
    }
}
