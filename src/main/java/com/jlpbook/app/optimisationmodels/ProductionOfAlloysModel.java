package com.jlpbook.app.optimisationmodels;

import com.jlpbook.app.model.PoaModelComponent;
import com.jlpbook.app.model.PoaRawMaterial;
import com.jlpbook.app.solver.model.Constraint;
import com.jlpbook.app.solver.model.ObjectiveFunction;
import com.jlpbook.app.solver.model.Term;
import com.jlpbook.app.solver.model.Variable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * The company Steel has received an order for 500 tonnes of steel to be used in shipbuilding. This steel
 * must have the following characteristics (‘grades’).
 * Table 6.1: Characteristics of steel ordered
 * Chemical element Minimum grade Maximum grade
 * Carbon (C) 2 3
 * Copper (Cu) 0.4 0.6
 * Manganese (Mn) 1.2 1.65
 * The company has seven different raw materials in stock that may be used for the production of this steel.
 * Table 6.2 lists the grades, available amounts and prices for all raw materials.
 * Table 6.2: Raw material grades, availabilities, and prices
 * Raw material C % Cu % Mn % Availability in t Cost in BC/t
 * Iron alloy 1 2.5 0 1.3 400 200
 * Iron alloy 2 3 0 0.8 300 250
 * Iron alloy 3 0 0.3 0 600 150
 * Copper alloy 1 0 90 0 500 220
 * Copper alloy 2 0 96 4 200 240
 * Aluminum alloy 1 0 0.4 1.2 300 200
 * Aluminum alloy 2 0 0.6 0 250 165
 * The objective is to determine the composition of the steel that minimizes the production cost
 *
 *
 * The required 500 tonnes of steel are produced with 400 tonnes of iron alloy 1, 39.776 tonnes of iron alloy
 * 3, 2.761 tonnes of copper alloy 2, and 57.462 tonnes of aluminum alloy 1. The percentages of Carbon,
 * Copper, and Manganese are 2%, 0.6%, and 1.2% respectively. The total cost of production is BC 98121.60
 */
public class ProductionOfAlloysModel extends AbstractOptimisationModel {
    int produceRequired;
    List<PoaModelComponent> components = new ArrayList<>();
    List<PoaRawMaterial> rawMaterials = new ArrayList<>();
    Variable produceVariable;
    Map<PoaModelComponent.ChemicalElement, Pair<Float, Float>> componentGrades;
    @Override
    public void init() {
        produceRequired = 500; //tonnes

        //Characteristics of steel ordered
        componentGrades = new HashMap<>();
        componentGrades.put(PoaModelComponent.ChemicalElement.CARBON, Pair.of(2.0f, 3.0f));
        componentGrades.put(PoaModelComponent.ChemicalElement.COPPER, Pair.of(0.4f, 0.6f));
        componentGrades.put(PoaModelComponent.ChemicalElement.Manganese, Pair.of(1.2f, 1.65f));

        componentGrades.forEach((k,v) -> {
            components.add(new PoaModelComponent(k, v.getLeft(), v.getRight()));
        });

        // Raw material grades, availabilities, and prices
        Map<String, List<Float>> materials = new HashMap<>();
        materials.put("Iron alloy 1", Arrays.asList(2.5f, 0.0f, 1.3f, 400.0f, 200.0f));
        materials.put("Iron alloy 2", Arrays.asList(3.0f, 0.0f, 0.8f, 300.0f, 250.0f));
        materials.put("Iron alloy 3", Arrays.asList(0.0f, 0.3f, 0.0f, 600.0f, 150.0f));
        materials.put("Copper alloy 1", Arrays.asList(0.0f, 90.0f, 0.0f, 500.0f, 220.0f));
        materials.put("Copper alloy 2", Arrays.asList(0.0f, 96.0f, 4.0f, 200.0f, 240.0f));
        materials.put("Aluminum alloy 1", Arrays.asList(0.0f, 0.4f, 1.2f, 300.0f, 200.0f));
        materials.put("Aluminum alloy 2", Arrays.asList(0.0f, 0.6f, 0.0f, 250.0f, 165.0f));

        materials.forEach((k,v) -> {
            PoaRawMaterial material = PoaRawMaterial.builder()
                    .name(k)
                    .build();
            material.getGradeMapping().put(PoaModelComponent.ChemicalElement.CARBON, v.get(0));
            material.getGradeMapping().put(PoaModelComponent.ChemicalElement.COPPER, v.get(1));
            material.getGradeMapping().put(PoaModelComponent.ChemicalElement.Manganese, v.get(2));

            material.setAvailability(v.get(3));
            material.setCost(v.get(4));

            rawMaterials.add(material);
        });

        objectiveFunction = ObjectiveFunction.MINIMISE; // minimise cost
    }

    @Override
    protected List<Constraint> populateConstraints() {
        List<Constraint> constraints = new ArrayList<>();
        constraints.add(getRequiredProduceConstraint());
        constraints.addAll(getComponentRequirementConstraint());
        constraints.addAll(getAvailabilityConstraint());
        constraints.add(getMinimumProduceConstraint());
        return constraints;
    }

    private Constraint getMinimumProduceConstraint() {
        String constraintName = "minimum produce";
        List<Term> LHS = new ArrayList<>();
        LHS.add(new Term(produceVariable.getIndex(), 1));
        return new Constraint(constraintName, LHS, Constraint.SENSE.GREATER_OR_EQUALS, produceRequired);
    }

    private List<Constraint> getAvailabilityConstraint() {
        List<Constraint> constraints = new ArrayList<>();
        for (PoaRawMaterial rawMaterial : rawMaterials) {
            List<Term> LHS = new ArrayList<>();
            LHS.add(new Term(rawMaterial.getUseQtyVariable().getIndex(), 1));
            String constraintName = String.format("availability of %s", rawMaterial.getName());
            constraints.add(new Constraint(constraintName, LHS, Constraint.SENSE.LESS_OR_EQUALS, rawMaterial.getAvailability()));
        }
        return constraints;
    }

    private List<Constraint> getComponentRequirementConstraint() {
        List<Constraint> constraints = new ArrayList<>();
        for (PoaModelComponent component : components) {
            List<Term> LHS = new ArrayList<>();
            for (PoaRawMaterial rawMaterial : rawMaterials) {
                LHS.add(new Term(rawMaterial.getUseQtyVariable().getIndex(), rawMaterial.getGradeMapping().get(component.getChemicalElement())));
            }

            List<Term> minLHS = new ArrayList<>(LHS);
            minLHS.add(new Term(produceVariable.getIndex(), -1 * componentGrades.get(component.getChemicalElement()).getLeft()));
            String constraintName = String.format("min %s", component.getChemicalElement().toString());
            constraints.add(new Constraint(constraintName, minLHS, Constraint.SENSE.GREATER_OR_EQUALS, 0));

            List<Term> maxLHS = new ArrayList<>(LHS);
            maxLHS.add(new Term(produceVariable.getIndex(), -1 * componentGrades.get(component.getChemicalElement()).getRight()));
            constraintName = String.format("max %s", component.getChemicalElement().toString());
            constraints.add(new Constraint(constraintName, maxLHS, Constraint.SENSE.LESS_OR_EQUALS, 0));
        }
        return constraints;
    }

    private Constraint getRequiredProduceConstraint() {
        List<Term> LHS = new ArrayList<>();
        for (PoaRawMaterial material : rawMaterials) {
            LHS.add(new Term(material.getUseQtyVariable().getIndex(), 1));
        }
        LHS.add(new Term(produceVariable.getIndex(), -1));

        return new Constraint("Qty of Steel", LHS, Constraint.SENSE.EQUALS, 0);
    }

    @Override
    protected void populateVariables() {
        produceVariable = Variable.builder()
                .index(variables.size() + 1)
                .name("total_produce")
                .variableType(Variable.VariableType.SEMI_CONTINUOUS)
                .objectiveCoefficient(0)
                .LB(produceRequired)
                .UB(Float.MAX_VALUE)
                .build();
        variables.put(produceVariable.getIndex(), produceVariable);

        rawMaterials.forEach(m -> m.populateVariable(variables));
    }
}
