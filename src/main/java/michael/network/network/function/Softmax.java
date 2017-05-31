package michael.network.network.function;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

public class Softmax implements Function{
    @Override
    public DoubleMatrix x(DoubleMatrix m) {
        DoubleMatrix exp = MatrixFunctions.exp(m);
        DoubleMatrix sums = exp.rowSums();
        return exp.diviColumnVector(sums);
    }

    @Override
    public DoubleMatrix dx(DoubleMatrix m) {
        DoubleMatrix M = x(m);
        return M.mul((M.mul(-1)).addi(1));
    }
    
}