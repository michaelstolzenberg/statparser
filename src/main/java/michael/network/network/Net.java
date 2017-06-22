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
    
    //test
    private Optimizer hiddena;
    private Optimizer outa;
  
    public Net(DoubleMatrix examples, DoubleMatrix results){
        this(examples,results,new NetParams());
    }
    
    public Net(DoubleMatrix examples, DoubleMatrix labels, NetParams params){
        this.params = params;
        this.batcher = new Batcher(examples,labels,params.batchSize);
        this.error = DoubleMatrix.zeros(1,1);
        this.hiddenLayer = new Layer(params.hiddenFunction,
                                     DoubleMatrix.randn(examples.columns,params.neurons).muli(0.01),
                                     DoubleMatrix.zeros(1,params.neurons).add(params.hiddenBias));
        this.outLayer = new Layer(params.outFunction,
                                  DoubleMatrix.randn(params.neurons,labels.columns).muli(0.01),
                                  DoubleMatrix.zeros(1,labels.columns).add(params.outBias));
    //test
        this.hiddena = new AdaDelta(params.neurons);
        this.outa = new AdaDelta(labels.columns);
    
    }
    
    private void forward(DoubleMatrix input,Layer layer){
        layer.sum = input.mmul(layer.weights).addRowVector(layer.bias);
        layer.activation = layer.function.x(layer.sum);
    }

    private double back(Layer out,Layer hidden,DoubleMatrix examples,DoubleMatrix labels){
    
//test        
        error = labels.sub(out.activation);
//cross entropy error
        //hidden.weights.print();
        //out.weights.print();
        //error = labels.mul(MatrixFunctions.log(out.activation)).mul(-1d);
        //double e = error.sum();
        DoubleMatrix gradient = out.function.dx(out.sum).mul(error);
        out.delta = DoubleMatrix.zeros(gradient.rows,gradient.columns);
        for(int i = 0;i<out.delta.rows;i++){
            out.delta.putRow(i,outa.putGradient(gradient.getRow(i)));
        }
        
        out.weightsChange = (hidden.activation.transpose().mmul(out.delta));
        //out.weightsChange.print();
        gradient = (out.delta.mmul(outLayer.weights.transpose())).mul(hidden.function.dx(hidden.sum));
        hidden.delta = DoubleMatrix.zeros(gradient.rows,gradient.columns);
        for(int i = 0;i<hidden.delta.rows;i++){
            hidden.delta.putRow(i,hiddena.putGradient(gradient.getRow(i)));
        }
        
        hidden.weightsChange = (examples.transpose().mmul(hidden.delta));
        
        //System.out.println(outLayer.weights.length); 
        //System.out.println(out.weightsChange.length+"\n");
        outLayer.weights.subi(out.weightsChange);
        //outLayer.bias.addi(out.delta.columnSums());
        hiddenLayer.weights.subi(hidden.weightsChange);
        //out.activation.print();
        //hiddenLayer.bias.addi(hidden.delta.columnSums().mul(params.learningRate));
         /*  
//gradient descent
        error = labels.sub(out.activation);
        //error.print();
        out.delta = (out.function.dx(out.sum)).mul(error);
        //out.delta.print();
        out.weightsChange = (out.delta.transpose().mmul(hidden.activation)).mul(params.learningRate);
        //out.weightsChange.print();
        hidden.delta = (out.delta.mmul(outLayer.weights.transpose())).mul(hidden.function.dx(hidden.sum));
        hidden.weightsChange = (hidden.delta.transpose().mmul(examples)).mul(params.learningRate);
//update
        outLayer.weights.addi(out.weightsChange);
        outLayer.bias.addi(out.delta.columnSums().mul(params.learningRate));
        hiddenLayer.weights.addi(hidden.weightsChange);
        hiddenLayer.bias.addi(hidden.delta.columnSums().mul(params.learningRate));
        */
         error = labels.mul(MatrixFunctions.log(out.activation)).mul(-1d);
        double e = error.sum();
        return e;
    }
    
    public void train(){
        while(batcher.hasNext()){
            Batch thisBatch = batcher.nextBatch();
            for(int i = 0;i<params.maxIter;i++){
                forward(thisBatch.examples,hiddenLayer);
                forward(hiddenLayer.activation,outLayer);
                System.out.println(back(outLayer,hiddenLayer,thisBatch.examples,thisBatch.labels));
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