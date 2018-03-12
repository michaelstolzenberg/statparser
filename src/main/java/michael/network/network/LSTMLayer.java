package michael.network.network;

import michael.network.network.function.Sigmoid;
import michael.network.network.optimizer.AdaDelta;
import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

/**
 *
 * @author michael
 */
public class LSTMLayer implements Layer{
    private int neurons;
    private LSTMSubLayer forgetLayer;
    private LSTMSubLayer inputLayer;
    private LSTMSubLayer cLayer;
    private LSTMSubLayer outLayer;
    private DoubleMatrix forwardInput;
    private DoubleMatrix backInput;
    private DoubleMatrix c;
    public DoubleMatrix h;
   
    public LSTMLayer(DoubleMatrix forwardInput, DoubleMatrix backInput){
        this.forgetLayer = new LSTMSubLayer(new Sigmoid(),new AdaDelta(),new AdaDelta(),new AdaDelta(),DoubleMatrix.rand(forwardInput.columns,neurons).mul(0.1).sub(0.05),DoubleMatrix.rand(neurons,neurons).mul(0.1).sub(0.05),DoubleMatrix.rand(1,neurons).add(0));
        this.inputLayer = new LSTMSubLayer(new Sigmoid(),new AdaDelta(),new AdaDelta(),new AdaDelta(),DoubleMatrix.rand(forwardInput.columns,neurons).mul(0.1).sub(0.05),DoubleMatrix.rand(neurons,neurons).mul(0.1).sub(0.05),DoubleMatrix.rand(1,neurons).add(0));
        this.cLayer = new LSTMSubLayer(new Sigmoid(),new AdaDelta(),new AdaDelta(),new AdaDelta(),DoubleMatrix.rand(forwardInput.columns,neurons).mul(0.1).sub(0.05),DoubleMatrix.rand(neurons,neurons).mul(0.1).sub(0.05),DoubleMatrix.rand(1,neurons).add(0));
        this.outLayer = new LSTMSubLayer(new Sigmoid(),new AdaDelta(),new AdaDelta(),new AdaDelta(),DoubleMatrix.rand(forwardInput.columns,neurons).mul(0.1).sub(0.05),DoubleMatrix.rand(neurons,neurons).mul(0.1).sub(0.05),DoubleMatrix.rand(1,neurons).add(0));
        this.forwardInput = forwardInput;
        this.backInput = backInput;
    }
    
    @Override
    public void forward(){
        forgetLayer.sum = (forwardInput.mmul(forgetLayer.wx)).add(h.mmul(forgetLayer.wh)).addRowVector(forgetLayer.bias);
        forgetLayer.activation = forgetLayer.function.x(forgetLayer.sum);
        
        inputLayer.sum = (forwardInput.mmul(inputLayer.wx)).add(h.mmul(inputLayer.wh)).addRowVector(inputLayer.bias);
        inputLayer.activation = inputLayer.function.x(inputLayer.sum);
        
        cLayer.sum = (forwardInput.mmul(cLayer.wx)).add(h.mmul(cLayer.wh)).addRowVector(cLayer.bias);
        cLayer.activation = cLayer.function.x(cLayer.sum);
        
        outLayer.sum = (forwardInput.mmul(outLayer.wx)).add(h.mmul(outLayer.wh)).addRowVector(outLayer.bias);
        outLayer.activation = outLayer.function.x(outLayer.sum);
        
        c = (c.mul(forgetLayer.activation)).add(cLayer.activation.mul(inputLayer.activation));
        h = outLayer.activation.mul(tanh(c));
    }

    @Override
    public void back(){
// gradient descent using xent
        outLayer.delta = labels.sub(outLayer.activation);
        outLayer.gradient = hiddenLayer.activation.transpose().mmul(outLayer.delta);
        hiddenLayer.delta = (outLayer.delta.mmul(outLayer.weights.transpose())).mul(hiddenLayer.function.dx(hiddenLayer.sum));
        hiddenLayer.gradient = examples.transpose().mmul(hiddenLayer.delta);
// update
        outLayer.weights.addi(outLayer.optimizer.optimizeGradient(outLayer.gradient));
        outLayer.bias.addi(outLayer.biasOptimizer.optimizeGradient(outLayer.delta.columnSums()));
        hiddenLayer.weights.addi(hiddenLayer.optimizer.optimizeGradient(hiddenLayer.gradient));
        hiddenLayer.bias.addi(hiddenLayer.biasOptimizer.optimizeGradient(hiddenLayer.delta.columnSums()));
    }
}