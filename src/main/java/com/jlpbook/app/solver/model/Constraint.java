package com.jlpbook.app.solver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lpsolve.LpSolve;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Constraint {
    public enum SENSE {
        EQUALS(LpSolve.EQ), GREATER_OR_EQUALS(LpSolve.GE), LESS_OR_EQUALS(LpSolve.LE);

        private final int value;

        SENSE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    String name;
    List<Term> LHS;
    SENSE sense;
    float RHS;
}
