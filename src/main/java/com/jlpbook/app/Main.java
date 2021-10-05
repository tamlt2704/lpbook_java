package com.jlpbook.app;

import com.jlpbook.app.optimisationmodels.AbstractOptimisationModel;
import com.jlpbook.app.optimisationmodels.ChessOptimisationModel;
import com.jlpbook.app.solver.LpModelSolver;
import com.jlpbook.app.solver.model.LpResult;

public class Main {
    public static void main(String[] args) {
        LpModelSolver solver = new LpModelSolver();

        AbstractOptimisationModel chessModel = new ChessOptimisationModel();
        chessModel.buildModel();

        LpResult result = solver.solve(chessModel.getModel());
        chessModel.printResult(result);
    }
}
