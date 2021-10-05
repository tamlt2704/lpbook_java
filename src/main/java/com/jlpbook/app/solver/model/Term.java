package com.jlpbook.app.solver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Term {
    int variableIndex;
    float coeff;
}
