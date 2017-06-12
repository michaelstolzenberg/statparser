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
    private DoubleMatrix gradAccum = DoubleMatrix.zeros(n);
    private DoubleMatrix updAccum = DoubleMatrix.zeros(n);
    private DoubleMatrix lr = DoubleMatrix.zeros(n);
    @Override
    public void putGradient(DoubleMatrix gradient){
        int n = gradient.columns;
        
        double p = 0.95; //decay rate
        double e = Math.pow(Math.E, -6); //constant
        for (int i=0; i<n; i++) {
            gradAccum.put(i, p * gradAccum.get(i) + (1d-p) * Math.pow(gradient.get(i),2));
            lr.put(i, Math.sqrt(updAccum.get(i) + e) / Math.sqrt(gradAccum.get(i) + e));
            double update = lr.get(i) * gradient.get(i);
            updAccum.put(i, p * updAccum.get(i) + (1d-p) * Math.pow(update,2));
        }
    }
    public DoubleMatrix getWeightsChange(DoubleMatrix m){
        
    }
    public DoubleMatrix getBiasChange(DoubleMatrix m){
        
    }
}
