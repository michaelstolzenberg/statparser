/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package michael.network.network.optimizer;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

/**
 *
 * @author michael
 */
public class AdaDelta implements Optimizer{
    
    private DoubleMatrix gradAccum;
    private DoubleMatrix updAccum;
    private DoubleMatrix lr;
    //private DoubleMatrix x0;
    private final double p = 0.95; //decay rate
    private final double e = Math.pow(Math.E, -6); //constant
    
    public AdaDelta(int n){
        this.gradAccum = DoubleMatrix.zeros(1,n);
        this.updAccum = DoubleMatrix.zeros(1,n);
        //this.gradAccum = DoubleMatrix.randn(1,n).addi(-0.5).mul(0.001);
        //this.updAccum = DoubleMatrix.randn(1,n).addi(-0.5).mul(0.001);
        this.lr = DoubleMatrix.zeros(1,n);
        //this.x0 = DoubleMatrix.zeros(1,n).add(0.1);
    }
    
    @Override
    public DoubleMatrix putGradient(DoubleMatrix gradient){
        gradAccum.muli(p).addi(MatrixFunctions.pow(gradient,2).muli(1d-p));
        lr=((MatrixFunctions.sqrt(updAccum.add(e)).div(MatrixFunctions.sqrt(gradAccum.add(e)))).muli(gradient)).muli(-1d);
        updAccum.muli(p).addi(MatrixFunctions.pow(lr,2).muli(1d-p));
        //gradient.print();   0,0506927230477906  0,0393541887224219
        /*for (int i=0;i<gradAccum.columns;i++){
            gradAccum.put(i, p * gradAccum.get(i) + (1d-p) * Math.pow(gradient.get(i),2));
            lr.put(i, Math.sqrt(updAccum.get(i) + e) / Math.sqrt(gradAccum.get(i) + e));
            double update = lr.get(i) * gradient.get(i);
            updAccum.put(i, p * updAccum.get(i) + (1d-p) * Math.pow(update,2));  
        }*/
        //updAccum.print();
        return updAccum;
    }
}
