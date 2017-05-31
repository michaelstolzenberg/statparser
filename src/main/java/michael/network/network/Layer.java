package michael.network.network;

import michael.network.network.function.Function;
import org.jblas.DoubleMatrix;

public class Layer {
        public final DoubleMatrix sum;
        public final DoubleMatrix activation;
        public final Function function;
        public Layer(DoubleMatrix sum,DoubleMatrix activation,Function function){
            this.sum = sum;
            this.activation = activation;
            this.function = function;
        }
    }