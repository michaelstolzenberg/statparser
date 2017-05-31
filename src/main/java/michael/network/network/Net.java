package michael.network.network;

import michael.network.network.function.Function;
import michael.network.network.function.Sigmoid;
import michael.network.network.function.Softmax;
import org.jblas.DoubleMatrix;

/**
 *
 * @author michael
 */
public class Net {
    public final NetParams params;
    private final Batcher batcher;
    private final DoubleMatrix hiddenIn;
    private final DoubleMatrix hiddenOut;
    
    public Net(DoubleMatrix examples, DoubleMatrix results){
        this(examples,results,new NetParams());
    }
    
    public Net(DoubleMatrix examples, DoubleMatrix results, NetParams params){
        this.params = params;
        this.batcher = new Batcher(examples,results,params.batchSize);
        this.hiddenIn = DoubleMatrix.randn(examples.columns,params.neurons);
        this.hiddenOut = DoubleMatrix.randn(params.neurons,results.columns);
    }
    
    private Layer forward(DoubleMatrix input,DoubleMatrix weights, Function function){
        DoubleMatrix sum = input.mmul(weights);
        DoubleMatrix activations = function.x(sum);
        return new Layer(sum,activations,function);
    }
    
    private DoubleMatrix back(Layer out,Layer hidden,DoubleMatrix examples,DoubleMatrix results){
        DoubleMatrix error = results.sub(out.activation);
        DoubleMatrix dSumOut = (out.function.dx(out.sum)).mul(error);
        DoubleMatrix hiddenOutChange = (hidden.activation.transpose().mmul(dSumOut)).mul(params.learningRate);
        DoubleMatrix dSumHidden = (dSumOut.mmul(hiddenOut.transpose())).mul(hidden.function.dx(hidden.sum));
        DoubleMatrix hiddenInChange = (examples.transpose().mmul(dSumHidden)).mul(params.learningRate);
        hiddenOut.addi(hiddenOutChange);
        hiddenIn.addi(hiddenInChange);
        return error;
    }
    
    public void train(){
        
        while(batcher.hasNext()){
            Batch thisBatch = batcher.nextBatch();
            for(int i = 0;i<params.maxIter;i++){
                Layer hidden = forward(thisBatch.examples,hiddenIn,params.hiddenFunction);
                Layer out = forward(hidden.activation,hiddenOut,params.outFunction);
//print for graph
                //out.activation.getRow(3).print();
                back(out,hidden,thisBatch.examples,thisBatch.results);
            }
        }
    }
    public DoubleMatrix predict(DoubleMatrix example){
        Layer hidden = forward(example,hiddenIn,params.hiddenFunction);
        Layer out = forward(hidden.activation,hiddenOut,params.outFunction);
        return out.activation;
    }
}