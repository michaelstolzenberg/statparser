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
public interface Optimizer {
    public void putGradient(DoubleMatrix gradient);
    public DoubleMatrix getWeightsChange(DoubleMatrix m);
    public DoubleMatrix getBiasChange(DoubleMatrix m);
}
