package com.jlpbook.app.model;

import com.jlpbook.app.solver.model.Variable;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class BuglarItem {
    int weight;
    int value;
    String name;
    Variable isSelectedVariable;

    public void populateVariable(Map<Integer, Variable> variables) {
        isSelectedVariable = Variable.builder()
                .index(variables.size()+1)
                .name(name)
                .variableType(Variable.VariableType.BINARY)
                .LB(0)
                .UB(1)
                .objectiveCoefficient(value)
                .build();
        variables.put(isSelectedVariable.getIndex(), isSelectedVariable);
    }
}
