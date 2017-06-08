package michael.network.network;

import michael.network.network.function.Function;
import org.jblas.DoubleMatrix;

/**
 *
 * @author michael
 */
public class Net {
    public final NetParams params;
    private final Batcher batcher;
    private final DoubleMatrix hiddenWeights;
    private final DoubleMatrix hiddenBias;
    private final DoubleMatrix outWeights;
    private final DoubleMatrix outBias;
    
    public Net(DoubleMatrix examples, DoubleMatrix results){
        this(examples,results,new NetParams());
    }
    
    public Net(DoubleMatrix examples, DoubleMatrix labels, NetParams params){
        this.params = params;
        this.batcher = new Batcher(examples,labels,params.batchSize);
        this.hiddenWeights = DoubleMatrix.randn(examples.columns,params.neurons);
        this.outWeights = DoubleMatrix.randn(params.neurons,labels.columns);
        this.hiddenBias = DoubleMatrix.zeros(1,params.neurons).add(params.hiddenBias);
        this.outBias = DoubleMatrix.zeros(1,labels.columns).add(params.outBias);
    }
    
    private Layer forward(DoubleMatrix input,DoubleMatrix weights, Function function, DoubleMatrix bias){
        DoubleMatrix sum = input.mmul(weights).addRowVector(bias);
        DoubleMatrix activations = function.x(sum);
        return new Layer(sum,activations,function);
    }
    
    private DoubleMatrix back(Layer out,Layer hidden,DoubleMatrix examples,DoubleMatrix labels){
        //gradient descent
        DoubleMatrix error = labels.sub(out.activation);
        DoubleMatrix outDelta = (out.function.dx(out.sum)).mul(error);
        DoubleMatrix outWeightsChange = (hidden.activation.transpose().mmul(outDelta)).mul(params.learningRate);
        DoubleMatrix hiddenDelta = (outDelta.mmul(outWeights.transpose())).mul(hidden.function.dx(hidden.sum));
        DoubleMatrix hiddenWeightsChange = (examples.transpose().mmul(hiddenDelta)).mul(params.learningRate);
        //update
        outWeights.addi(outWeightsChange);
        outBias.addi(outDelta.columnSums().mul(params.learningRate));
        hiddenWeights.addi(hiddenWeightsChange);
        hiddenBias.addi(hiddenDelta.columnSums().mul(params.learningRate));
        
        return error;
    }
    
    public void train(){
        Layer hidden;
        Layer out;
        while(batcher.hasNext()){
            Batch thisBatch = batcher.nextBatch();
            for(int i = 0;i<params.maxIter;i++){
                hidden = forward(thisBatch.examples,hiddenWeights,params.hiddenFunction,hiddenBias);
                out = forward(hidden.activation,outWeights,params.outFunction,outBias);
//print for graph
                back(out,hidden,thisBatch.examples,thisBatch.results);
            }
        }
    }
    public DoubleMatrix predict(DoubleMatrix example){
        Layer hidden = forward(example,hiddenWeights,params.hiddenFunction,hiddenBias);
        Layer out = forward(hidden.activation,outWeights,params.outFunction,outBias);
        return out.activation;
    }
}