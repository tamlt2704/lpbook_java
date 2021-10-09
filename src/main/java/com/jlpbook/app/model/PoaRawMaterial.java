package com.jlpbook.app.model;

import com.jlpbook.app.solver.model.Variable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class PoaRawMaterial {
    String name;
    @Builder.Default
    Map<PoaModelComponent.ChemicalElement, Float> gradeMapping = new HashMap<>();
    float availability; // in ton
    double cost; // euro / ton

    Variable useQtyVariable;

    public void populateVariable(Map<Integer, Variable> variables) {
        useQtyVariable = Variable.builder()
                .index(variables.size()+1)
                .name(name)
                .variableType(Variable.VariableType.SEMI_CONTINUOUS)
                .LB(0)
                .UB(Float.MAX_VALUE)
                .objectiveCoefficient(cost)
                .build();

        variables.put(useQtyVariable.getIndex(), useQtyVariable);
    }
}
