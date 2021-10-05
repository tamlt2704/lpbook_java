package com.jlpbook.app.solver.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class LpResult {
    public enum SOLVER_STATUS {
        UNKNOWNERROR (-5),
        DATAIGNORED  (-4),
        NOBFP        (-3),
        NOMEMORY     (-2),
        NOTRUN       (-1),
        OPTIMAL      ( 0),
        SUBOPTIMAL   ( 1),
        INFEASIBLE   ( 2),
        UNBOUNDED    ( 3),
        DEGENERATE   ( 4),
        NUMFAILURE   ( 5),
        USERABORT    ( 6),
        TIMEOUT      ( 7),
        RUNNING      ( 8),
        PRESOLVED    ( 9);

        private final int value;

        SOLVER_STATUS(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    double objectiveValue;
    Map<Integer, Double> variableResult;
    SOLVER_STATUS solverStatus;
}
