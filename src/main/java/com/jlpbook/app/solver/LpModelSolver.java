package com.jlpbook.app.solver;

import com.jlpbook.app.solver.model.*;
import lombok.extern.slf4j.Slf4j;
import lpsolve.LpSolve;
import lpsolve.LpSolveException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class LpModelSolver {
    public LpResult solve(LpModel lpModel) {
        try {
            int nbConstraints = lpModel.getConstraints().size();
            int nbVariables = lpModel.getVariables().size();
            LpSolve solver = LpSolve.makeLp(0, nbVariables);

            //add constraints
            int constrCount = 1;
            solver.setAddRowmode(true);
            for (Constraint constraint : lpModel.getConstraints()) {
                int k = 0;
                int nbVariableInConstraint = constraint.getLHS().size();
                int[] colNo = new int[nbVariableInConstraint];
                double[] row = new double[nbVariableInConstraint];
                for (Term term : constraint.getLHS()) {
                    colNo[k] = term.getVariableIndex();
                    row[k++] = term.getCoeff();
                }
                solver.addConstraintex(nbConstraints, row, colNo, constraint.getSense().getValue(), constraint.getRHS());
                log.info("constraint {}: {}", constrCount, constraint.getName());
                solver.setRowName(constrCount++, constraint.getName());
            }
            solver.setAddRowmode(false);

            // objective
            int k = 0;
            int[] colNo = new int[nbVariables];
            double[] row = new double[nbVariables];
            for (Variable variable : lpModel.getVariables()) {
                colNo[k] = variable.getIndex();
                row[k++] = variable.getObjectiveCoefficient();
            }
            solver.setObjFnex(k, row, colNo);

            if (lpModel.getObjectiveFunction().equals(ObjectiveFunction.MINIMISE)) {
                solver.setMinim();
            } else {
                solver.setMaxim();
            }

            // set variable types
            for (Variable variable : lpModel.getVariables()) {
                switch (variable.getVariableType()) {
                    case BINARY:
                        solver.setBinary(variable.getIndex(), true);
                        break;
                    case INTEGER:
                        solver.setInt(variable.getIndex(), true);
                        break;
                    case SEMI_CONTINUOUS:
                        solver.setSemicont(variable.getIndex(), true);
                        break;
                    case CONTINUOUS:
                        break;
                }

                solver.setColName(variable.getIndex(), variable.getName());
                solver.setLowbo(variable.getIndex(), variable.getLB());
                solver.setUpbo(variable.getIndex(), variable.getUB());
            }


            solver.writeLp("abc.LP");
            int solverStatus = solver.solve();

            return getSolverResult(solver, solverStatus, nbVariables);
        } catch (LpSolveException e) {
            e.printStackTrace();
        }
        return null;
    }

    private LpResult getSolverResult(LpSolve solver, int solverStatus, int nbVariables) {
        try {
            double[] varVals = new double[nbVariables];
            solver.getVariables(varVals);
            Map<Integer, Double> varValMap = new HashMap<>();
            for (int i = 0; i < varVals.length; i++) {
                varValMap.put(i+1, varVals[i]);
            }

            double objValue = solver.getObjective();

            LpResult lpResult = LpResult.builder()
                    .solverStatus(LpResult.SOLVER_STATUS.values()[solverStatus])
                    .variableResult(varValMap)
                    .objectiveValue(objValue)
                    .build();
            return lpResult;
        } catch (LpSolveException e) {
            e.printStackTrace();
        }
        return null;
    }
}
