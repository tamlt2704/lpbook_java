package com.jlpbook.app.model;

import com.jlpbook.app.solver.model.Variable;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Chess {
    public enum ChessType {
        SMALL, LARGE
    }

    Variable numberOfChessProducedVariable;

    ChessType chessType;
    float profit;
    float kgWoodRequired; // kg
    float latheHoursRequired;

    public void populateVariables(Map<Integer, Variable> variables) {
        numberOfChessProducedVariable = Variable.builder()
                .index(variables.size()+1)
                .name(chessType.toString())
                .variableType(Variable.VariableType.INTEGER)
                .LB(0)
                .UB(Integer.MAX_VALUE)
                .objectiveCoefficient(profit)
                .build();
        variables.put(numberOfChessProducedVariable.getIndex(), numberOfChessProducedVariable);
    }
}
