package com.jlpbook.app.optimisationmodels;

import com.jlpbook.app.solver.VariableFactory;
import com.jlpbook.app.solver.model.*;

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

    public LpModel getModel() {
        return lpModel;
    }

    public abstract void printResult(LpResult result);

    protected abstract List<Constraint> populateConstraints();

    protected abstract void populateVariables();
}
