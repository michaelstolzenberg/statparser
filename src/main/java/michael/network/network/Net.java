package michael.network.network;

import org.jblas.DoubleMatrix;

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
                                     DoubleMatrix.randn(examples.columns,params.neurons),
                                     DoubleMatrix.zeros(1,params.neurons).add(params.hiddenBias));
        this.outLayer = new Layer(params.outFunction,
                                  DoubleMatrix.randn(params.neurons,labels.columns),
                                  DoubleMatrix.zeros(1,labels.columns).add(params.outBias));
    }
    
    private void forward(DoubleMatrix input,Layer layer){
        layer.sum = input.mmul(layer.weights).addRowVector(layer.bias);
        layer.activation = layer.function.x(layer.sum);
    }
    
    private DoubleMatrix back(Layer out,Layer hidden,DoubleMatrix examples,DoubleMatrix labels){
//gradient descent
// todo adadelta
        error = labels.sub(out.activation);
        out.delta = (out.function.dx(out.sum)).mul(error);
        out.weightsChange = (hidden.activation.transpose().mmul(out.delta)).mul(params.learningRate);
        hidden.delta = (out.delta.mmul(outLayer.weights.transpose())).mul(hidden.function.dx(hidden.sum));
        hidden.weightsChange = (examples.transpose().mmul(hidden.delta)).mul(params.learningRate);
//update
        outLayer.weights.addi(out.weightsChange);
        outLayer.bias.addi(out.delta.columnSums().mul(params.learningRate));
        hiddenLayer.weights.addi(hidden.weightsChange);
        hiddenLayer.bias.addi(hidden.delta.columnSums().mul(params.learningRate));
        
        return error;
    }
    
    public void train(){
        while(batcher.hasNext()){
            Batch thisBatch = batcher.nextBatch();
            for(int i = 0;i<params.maxIter;i++){
                forward(thisBatch.examples,hiddenLayer);
                forward(hiddenLayer.activation,outLayer);
                back(outLayer,hiddenLayer,thisBatch.examples,thisBatch.labels);
            }
        }
    }
    public DoubleMatrix predict(DoubleMatrix example){
        forward(example,hiddenLayer);
        forward(hiddenLayer.activation,outLayer);
        return outLayer.activation;
    }
}