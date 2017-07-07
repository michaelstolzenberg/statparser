package michael.network.network;

import michael.network.network.optimizer.AdaDelta;
import michael.network.network.optimizer.Optimizer;
import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

/**
 *
 * @author michael
 */
public class Net {
    public final NetParams params;
    private final Batcher batcher;
    private DoubleMatrix error;
    private Layer hiddenLayer;
    private Layer outLayer;
  
    public Net(DoubleMatrix examples, DoubleMatrix results){
        this(examples,results,new NetParams());
    }
    
    public Net(DoubleMatrix examples, DoubleMatrix labels, NetParams params){
        this.params = params;
        this.batcher = new Batcher(examples,labels,params.batchSize);
        this.error = DoubleMatrix.zeros(1,1);
        this.hiddenLayer = new Layer(params.hiddenFunction,
                                     params.hiddenOptimizer,
                                     DoubleMatrix.randn(examples.columns,params.neurons).mul(0.1),
                                     DoubleMatrix.zeros(1,params.neurons).add(params.hiddenBias));
        this.outLayer = new Layer(params.outFunction,
                                  params.outOptimizer,
                                  DoubleMatrix.randn(params.neurons,labels.columns).mul(0.1),
                                  DoubleMatrix.zeros(1,labels.columns).add(params.outBias));
    }
    
    private void forward(DoubleMatrix input,Layer layer){
        layer.sum = input.mmul(layer.weights).addRowVector(layer.bias);
        layer.activation = layer.function.x(layer.sum);
    }

    private double back(DoubleMatrix examples,DoubleMatrix labels){
// add regularization
// look at initialization glorot 2010
// ELU, dropout
        error = labels.mul(MatrixFunctions.log(outLayer.activation)).mul(-1d);
        outLayer.delta = labels.sub(outLayer.activation);
        outLayer.gradient = hiddenLayer.activation.transpose().mmul(outLayer.delta);
        outLayer.weightsChange = outLayer.optimizer.optimizeGradient(outLayer.gradient);
        hiddenLayer.delta = (outLayer.delta.mmul(outLayer.weights.transpose())).mul(hiddenLayer.function.dx(hiddenLayer.sum));
        hiddenLayer.gradient = examples.transpose().mmul(hiddenLayer.delta);
        hiddenLayer.weightsChange = hiddenLayer.optimizer.optimizeGradient(hiddenLayer.gradient);
//update
        outLayer.weights.addi(outLayer.weightsChange);
        outLayer.bias.addi(outLayer.delta.columnSums().mul(params.learningRate));
        hiddenLayer.weights.addi(hiddenLayer.weightsChange);
        hiddenLayer.bias.addi(hiddenLayer.delta.columnSums().mul(params.learningRate));
        return error.sum();
    }
    
    public void train(){
        while(batcher.hasNext()){
            Batch thisBatch = batcher.nextBatch();
            for(int i = 0;i<params.maxIter;i++){
                forward(thisBatch.examples,hiddenLayer);
                forward(hiddenLayer.activation,outLayer);
                System.out.println(back(thisBatch.examples,thisBatch.labels));
            }  
        }
    }
    public DoubleMatrix predict(DoubleMatrix example){
        forward(example,hiddenLayer);
        forward(hiddenLayer.activation,outLayer);
        return outLayer.activation;
    }
    
    
    
    
    
    public static void main(String[] args) {
        double[][] e = new double[4][2];
        e[0] = new double[]{0,0};
        e[1] = new double[]{1,0};
        e[2] = new double[]{0,1};
        e[3] = new double[]{1,1};
        DoubleMatrix examples = new DoubleMatrix(e);
        double[][] l = new double[4][4];
        l[0]=new double[]{1,0,0,0};
        l[1]=new double[]{0,1,0,0};
        l[2]=new double[]{0,0,1,0};
        l[3]=new double[]{0,0,0,1};
        DoubleMatrix labels = new DoubleMatrix(l);
        
        
        Net net = new Net(examples,labels);
        net.train();
        
        net.predict(examples.getRow(0)).print();
        net.predict(examples.getRow(1)).print();
        net.predict(examples.getRow(2)).print();
        net.predict(examples.getRow(3)).print();
    }
}