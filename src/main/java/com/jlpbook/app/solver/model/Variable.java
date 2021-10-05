package com.jlpbook.app.solver.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Variable {
    public enum VariableType {
        CONTINUOUS, SEMI_CONTINUOUS, BINARY, SEMI_INTEGER, INTEGER
    }

    int index;
    String name;
    VariableType variableType;
    float objectiveCoefficient;
    double value;
    float LB;
    float UB;
}
