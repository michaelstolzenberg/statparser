package michael.network.network;

import michael.network.network.function.Function;
import org.jblas.DoubleMatrix;

public class Layer {
    public final Function function;
    public final DoubleMatrix weights;
    public final DoubleMatrix bias;    
    public DoubleMatrix sum;
    public DoubleMatrix activation;
    public DoubleMatrix delta;
    public DoubleMatrix weightsChange;
    
    public Layer(Function function, DoubleMatrix weights, DoubleMatrix bias){
        this.function = function;
        this.weights = weights;
        this.bias = bias;
        this.sum = DoubleMatrix.zeros(1,1);
        this.activation = DoubleMatrix.zeros(1,1);
        this.delta = DoubleMatrix.zeros(1,1);
        this.weightsChange = DoubleMatrix.zeros(1,1);
    }
}