/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package michael.network.network.optimizer;

import org.jblas.DoubleMatrix;

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
        this.lr = DoubleMatrix.zeros(1,n);
        //this.x0 = DoubleMatrix.zeros(1,n).add(0.1);
    }
    
    @Override
    public DoubleMatrix putGradient(DoubleMatrix gradient, DoubleMatrix weights){
        for (int i=0;i<gradAccum.columns;i++){
            gradAccum.put(i, p * gradAccum.get(i) + (1d-p) * Math.pow(gradient.get(i),2));
            lr.put(i, Math.sqrt(updAccum.get(i) + e) / Math.sqrt(gradAccum.get(i) + e));
            double update = lr.get(i) * gradient.get(i);
            updAccum.put(i, p * updAccum.get(i) + (1d-p) * Math.pow(update,2));  
        }
        return updAccum;
    }
}
