package com.jlpbook.app.optimisationmodels;

import com.jlpbook.app.solver.VariableFactory;
import com.jlpbook.app.solver.model.*;
import de.vandermeer.asciitable.AsciiTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractOptimisationModel {
    protected Map<Integer, Variable> variables = new HashMap<>();
    private VariableFactory variableFactory = new VariableFactory(variables);
    LpModel lpModel = new LpModel();
    ObjectiveFunction objectiveFunction = ObjectiveFunction.MINIMISE;

    public abstract void init();

    public void buildModel() {
        init();
        populateVariables();
        lpModel.addVariables(new ArrayList<>(variables.values()));
        lpModel.addConstraints(populateConstraints());
        lpModel.setObjectiveFunction(objectiveFunction);
    }

    public void loadResult(LpResult result) {
        result.getVariableResult().forEach((vIndex, vVal) -> {
            variables.get(vIndex).setValue(vVal);
        });
    }

    public LpModel getModel() {
        return lpModel;
    }

    public void printResult(LpResult result) {
        AsciiTable at = new AsciiTable();

        at.addRule();
        at.addRow("Solver status", result.getSolverStatus().toString());
        at.addRule();

        at.addRow("Objective Function ", result.getObjectiveValue());
        at.addRule();

        Map<Integer, Double> varMapping = result.getVariableResult();
        for (Variable variable : variables.values()) {
            at.addRow(variable.getName(), varMapping.get(variable.getIndex()));
            at.addRule();
        }
        System.out.println(at.render());
    }

    protected abstract List<Constraint> populateConstraints();

    protected abstract void populateVariables();
}
