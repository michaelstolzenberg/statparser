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
        //DoubleMatrix M = x(m);
        //return M.mul((M.mul(-1)).addi(1));
        //jacobian of the softmax function
        DoubleMatrix M = new DoubleMatrix(m.columns,m.columns); 
        for(int i = 0;i<M.columns;i++){
            for(int j = 0;j<M.columns;j++){
                M.put(i,j,m.get(i)*(kd(i,j)-m.get(j)));
            }
        }
        return M;
    }
    //jacobian of the softmax function
    public DoubleMatrix jacobian(DoubleMatrix m){
        DoubleMatrix M = new DoubleMatrix(m.columns,m.columns); 
        for(int i = 0;i<M.columns;i++){
            for(int j = 0;j<M.columns;j++){
                M.put(i,j,m.get(i)*(kd(i,j)-m.get(j)));
            }
        }
        return M;
    }
    //kronecker delta
    private double kd(double i, double j){
        if (i == j) return 1d;
        return 0d;
    }
    
}