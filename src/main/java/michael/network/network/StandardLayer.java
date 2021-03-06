package michael.network.network;

import michael.network.network.function.Function;
import michael.network.network.optimizer.Optimizer;
import org.jblas.DoubleMatrix;

public class StandardLayer {
    public final Function function;
    public final DoubleMatrix weights;
    public final DoubleMatrix bias; 
    public DoubleMatrix gradient;
    public DoubleMatrix sum;
    public DoubleMatrix activation;
    public DoubleMatrix delta;
    public DoubleMatrix weightsChange;
    public Optimizer optimizer;
    public Optimizer biasOptimizer;
    
    public StandardLayer(Function function, Optimizer optimizer,Optimizer biasOptimizer,DoubleMatrix weights, DoubleMatrix bias){
        this.function = function;
        this.optimizer = optimizer;
        this.biasOptimizer = biasOptimizer;
        this.weights = weights;
        this.gradient = DoubleMatrix.zeros(weights.rows,weights.columns);
        this.bias = bias;
        this.sum = DoubleMatrix.zeros(1,1);
        this.activation = DoubleMatrix.zeros(1,1);
        this.delta = DoubleMatrix.zeros(1,1);
        this.weightsChange = DoubleMatrix.zeros(1,1);
    }
}