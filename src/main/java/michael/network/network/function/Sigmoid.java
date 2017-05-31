package michael.network.network.function;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

public class Sigmoid implements Function{
    @Override
    public DoubleMatrix x(DoubleMatrix M) {
        DoubleMatrix Denom = (MatrixFunctions.exp(M.mul(-1))).addi(1);
        return Denom.rdivi(1);
    }

    @Override
    public DoubleMatrix dx(DoubleMatrix X) {
        DoubleMatrix M = x(X);
        return M.mul((M.mul(-1)).addi(1));
    }
} 
