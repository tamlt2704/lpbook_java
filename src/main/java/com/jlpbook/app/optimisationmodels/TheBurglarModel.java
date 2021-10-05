package com.jlpbook.app.optimisationmodels;

import com.jlpbook.app.model.BuglarItem;
import com.jlpbook.app.model.Chess;
import com.jlpbook.app.solver.model.*;
import de.vandermeer.asciitable.AsciiTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A burglar sees eight items, of different values and weights. He wants to take the items of greatest total
 * value whose total weight is not more than the maximum WTMAX he can carry
 */
public class TheBurglarModel extends AbstractOptimisationModel {
    List<BuglarItem> buglarItems = new ArrayList<>();
    float WTMAX;
    int[] values;
    int[] weights;

    @Override
    public void init() {
        values = new int[]{15, 100, 90, 60, 40, 15, 10, 1};
        weights = new int[]{2, 20, 20, 30, 40, 30, 60, 10};
        for (int i = 0; i < values.length; i++) {
            buglarItems.add(BuglarItem.builder()
                    .name(String.format("take(%s)", i + 1))
                    .weight(weights[i])
                    .value(values[i])
                    .build());
        }
        WTMAX = 102;
        objectiveFunction = ObjectiveFunction.MAXIMISE;
    }

    @Override
    protected List<Constraint> populateConstraints() {
        List<Constraint> constraints = new ArrayList<>();
        List<Term> LHS = new ArrayList<>();
        for (BuglarItem item : buglarItems) {
            LHS.add(new Term(item.getIsSelectedVariable().getIndex(), item.getWeight()));
        }

        Constraint constraint = new Constraint("Weight Restriction", LHS, Constraint.SENSE.LESS_OR_EQUALS, WTMAX);
        constraints.add(constraint);
        return constraints;
    }

    @Override
    protected void populateVariables() {
        buglarItems.forEach(it -> it.populateVariable(variables));
    }
}
