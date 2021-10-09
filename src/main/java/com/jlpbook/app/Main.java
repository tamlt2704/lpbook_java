package com.jlpbook.app;

import com.jlpbook.app.optimisationmodels.AbstractOptimisationModel;
import com.jlpbook.app.optimisationmodels.ChessOptimisationModel;
import com.jlpbook.app.optimisationmodels.ProductionOfAlloysModel;
import com.jlpbook.app.optimisationmodels.TheBurglarModel;
import com.jlpbook.app.solver.LpModelSolver;
import com.jlpbook.app.solver.model.LpResult;

public class Main {
    public static void main(String[] args) {
        LpModelSolver solver = new LpModelSolver();

//        AbstractOptimisationModel optimisationModel = new ChessOptimisationModel();
//        AbstractOptimisationModel optimisationModel = new TheBurglarModel();
        AbstractOptimisationModel optimisationModel = new ProductionOfAlloysModel();

        optimisationModel.buildModel();
        LpResult result = solver.solve(optimisationModel.getModel());
        optimisationModel.loadResult(result);
        optimisationModel.printResult(result);
    }
}
