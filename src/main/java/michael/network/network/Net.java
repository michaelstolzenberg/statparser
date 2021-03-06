package michael.network.network;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

/**
 *
 * @author michael
 */
public class Net {
    public final NetParams params;
    private final Batcher batcher;
    private StandardLayer hiddenLayer;
    private StandardLayer outLayer;
    private boolean doDropout = false;
    private Dropout dropout;
    private Difficulty difficulty;
        
    public Net(DoubleMatrix examples, DoubleMatrix results){
        this(examples,results,new NetParams());
    }
    
    public Net(DoubleMatrix examples, DoubleMatrix labels, NetParams params){
        this.params = params;
        this.batcher = new Batcher(examples,labels,params.batchSize);
        this.hiddenLayer = new StandardLayer(params.hiddenFunction,
                                     params.hiddenOptimizer,
                                     params.hiddenBiasOptimizer,
                                     DoubleMatrix.rand(examples.columns,params.neurons).mul(0.1).sub(0.05),
                                     DoubleMatrix.zeros(1,params.neurons).add(params.hiddenBias));
        this.outLayer = new StandardLayer(params.outFunction,
                                  params.outOptimizer,
                                  params.outBiasOptimizer,
                                  DoubleMatrix.rand(params.neurons,labels.columns).mul(0.1).sub(0.05),
                                  DoubleMatrix.zeros(1,labels.columns).add(params.outBias));
        if(params.hiddenDropoutProbability>0d){
            dropout = new Dropout(params.hiddenDropoutProbability,params.neurons);
            doDropout = true;
        }
        this.difficulty = new Difficulty();
    }
    
    private void forward(DoubleMatrix input,StandardLayer layer){
        layer.sum = input.mmul(layer.weights).addRowVector(layer.bias);
        layer.activation = layer.function.x(layer.sum);
    }

    private double back(DoubleMatrix examples,DoubleMatrix labels){
    
// gradient descent using xent
        outLayer.delta = labels.sub(outLayer.activation);
    //test
        //outLayer.delta.print();
        //difficulty.getDiff(outLayer.delta);
        //outLayer.delta.print();
    //endtest
        outLayer.gradient = hiddenLayer.activation.transpose().mmul(outLayer.delta);
        hiddenLayer.delta = (outLayer.delta.mmul(outLayer.weights.transpose())).mul(hiddenLayer.function.dx(hiddenLayer.sum));
// dropout 
        if(doDropout){
            hiddenLayer.delta.muliRowVector(dropout.hiddenMask);
        }
        
        hiddenLayer.gradient = examples.transpose().mmul(hiddenLayer.delta);
        
// update
        outLayer.weights.addi(outLayer.optimizer.optimizeGradient(outLayer.gradient));
        outLayer.bias.addi(outLayer.biasOptimizer.optimizeGradient(outLayer.delta.columnSums()));
        hiddenLayer.weights.addi(hiddenLayer.optimizer.optimizeGradient(hiddenLayer.gradient));
        hiddenLayer.bias.addi(hiddenLayer.biasOptimizer.optimizeGradient(hiddenLayer.delta.columnSums()));
// return error
        return labels.mul(MatrixFunctions.log(outLayer.activation)).mul(-1d).columnMeans().sum();
    }
    
    public void train(){
        double error=0;
        //while(batcher.hasNext()){
            Batch thisBatch = batcher.nextBatch();
            //test
            difficulty.addY(thisBatch.labels);
            //endtest
            if(doDropout){
                dropout.createMasks();
            }
            for(int i = 0;i<params.maxIter;i++){
                forward(thisBatch.examples,hiddenLayer);
                if(doDropout){
                    hiddenLayer.activation.muliRowVector(dropout.hiddenMask);
                }
                forward(hiddenLayer.activation,outLayer);
                error = back(thisBatch.examples,thisBatch.labels);
            }
            //System.out.print(error);
        //}
        //hiddenLayer.weights.print();
    }
    
    public DoubleMatrix predict(DoubleMatrix example){
        forward(example,hiddenLayer);
        if(doDropout){
            hiddenLayer.activation.muli(1-params.hiddenDropoutProbability);
        }
        forward(hiddenLayer.activation,outLayer);
        //System.out.println(outLayer.activation);
        return outLayer.activation;
    }
    
// test
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