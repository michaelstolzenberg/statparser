package michael.network.network;

import michael.network.network.function.Function;
import michael.network.network.optimizer.Optimizer;
import org.jblas.DoubleMatrix;

public class LSTMSubLayer{
    public final Function function;
    public final DoubleMatrix wh;
    public final DoubleMatrix wx;
    public final DoubleMatrix bias;
    public DoubleMatrix gradientx;
    public DoubleMatrix gradienth;
    public DoubleMatrix sum;
    public DoubleMatrix activation;
    public DoubleMatrix deltah;
    public DoubleMatrix deltax;
    public DoubleMatrix weightsChangeh;
    public DoubleMatrix weightsChangex;
    public Optimizer optimizerh;
    public Optimizer optimizerx;
    public Optimizer biasOptimizer;

    
    public LSTMSubLayer(Function function, Optimizer optimizerh, Optimizer optimizerx, Optimizer biasOptimizer,DoubleMatrix wx, DoubleMatrix wh, DoubleMatrix bias){
        this.function = function;
        this.optimizerh = optimizerh;
        this.optimizerx = optimizerx;
        this.biasOptimizer = biasOptimizer;
        this.wh = wh;
        this.wx = wx;
        this.gradienth = DoubleMatrix.zeros(wh.rows,wh.columns);
        this.gradientx = DoubleMatrix.zeros(wx.rows,wx.columns);
        this.bias = bias;
        this.sum = DoubleMatrix.zeros(1,1);
        this.activation = DoubleMatrix.zeros(1,1);
        this.deltah = DoubleMatrix.zeros(1,1);
        this.deltax = DoubleMatrix.zeros(1,1);
        this.weightsChangeh = DoubleMatrix.zeros(1,1);
        this.weightsChangex = DoubleMatrix.zeros(1,1);
    }
}