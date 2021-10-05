package com.jlpbook.app.optimisationmodels;

import com.jlpbook.app.solver.model.*;
import com.jlpbook.app.model.Chess;
import de.vandermeer.asciitable.AsciiTable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A small joinery makes two different sizes of boxwood chess sets. The small set requires 3 hours of machining on a lathe, and the large set requires 2 hours. There are four lathes with skilled operators who
 * each work a 40 hour week, so we have 160 lathe-hours per week. The small chess set requires 1 kg of
 * boxwood, and the large set requires 3 kg. Unfortunately, boxwood is scarce and only 200 kg per week
 * can be obtained. When sold, each of the large chess sets yields a profit of $20, and one of the small chess
 * set has a profit of $5. The problem is to decide how many sets of each kind should be made each week
 * to so as to maximize profit.
 */
@Data
@Slf4j
public class ChessOptimisationModel extends AbstractOptimisationModel {
    private List<Chess> chessList = new ArrayList<>();
    private float latheHoursLimit = 160;
    private float boxWoodAvailableLimit = 200;

    @Override
    public void init() {
        chessList.add(Chess.builder()
                .chessType(Chess.ChessType.SMALL)
                .latheHoursRequired(3)
                .kgWoodRequired(1)
                .profit(5)
                .build());

        chessList.add(Chess.builder()
                .chessType(Chess.ChessType.LARGE)
                .latheHoursRequired(2)
                .kgWoodRequired(3)
                .profit(20)
                .build());
        objectiveFunction = ObjectiveFunction.MAXIMISE;
    }

    @Override
    protected List<Constraint> populateConstraints() {
        List<Constraint> constraints = new ArrayList<>();
        constraints.add(getLatheHourLimitConstraint());
        constraints.add(getBoxWoodLimitConstraint());
        return constraints;
    }

    private Constraint getBoxWoodLimitConstraint() {
        String constrName = "Kg of boxwood";
        List<Term> terms = new ArrayList<>();
        for (Chess chess : chessList) {
            terms.add(new Term(chess.getNumberOfChessProducedVariable().getIndex(), chess.getKgWoodRequired()));
        }
        return new Constraint(constrName, terms, Constraint.SENSE.LESS_OR_EQUALS, boxWoodAvailableLimit);
    }

    private Constraint getLatheHourLimitConstraint() {
        String constrName = "Lathehours";
        List<Term> terms = new ArrayList<>();
        for (Chess chess : chessList) {
            terms.add(new Term(chess.getNumberOfChessProducedVariable().getIndex(), chess.getLatheHoursRequired()));
        }
        return new Constraint(constrName, terms, Constraint.SENSE.LESS_OR_EQUALS, latheHoursLimit);
    }

    @Override
    protected void populateVariables() {
        chessList.forEach(c -> c.populateVariables(variables));
    }
}
