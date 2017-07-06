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
    private boolean init = true;
    private DoubleMatrix gradAccum;
    private DoubleMatrix updAccum;
    private DoubleMatrix update;
    private final double p = 0.95; //decay rate
    private final double e = Math.pow(Math.E, -6); //constant
    
    public AdaDelta(int n,int m){
        this.gradAccum = DoubleMatrix.zeros(n,m);
        this.updAccum = DoubleMatrix.zeros(n,m);
        this.update = DoubleMatrix.zeros(n,m);
    }
    
    @Override
    public DoubleMatrix putGradient(DoubleMatrix gradient){
//initialize x0
            if (init){
                gradAccum.addi(MatrixFunctions.abs(gradient));
                (updAccum.addi(MatrixFunctions.abs(gradient))).mul(0.01);
                init = false;
            }
            else{
//accumulate gradients: E[g2]t = pE[g2]t-1 + (1-p)g2
                (gradAccum.muli(p)).addi(MatrixFunctions.pow(gradient,2).mul(1d-p));
//compute update: Dxt = -(  RMS[Dx]t-1  /  RMS[g]t  ) gt            
                update=((MatrixFunctions.sqrt(updAccum.add(e))).div(MatrixFunctions.sqrt(gradAccum.add(e)))).mul(gradient).mul(-1d);
//accumulate updates: E[Dx2]t = pE[Dx2]t-1 + (1-p)Dx2t
                (updAccum.muli(p)).addi(MatrixFunctions.pow(update,2).mul(1d-p));
            }
        
        return update;
    }
}
