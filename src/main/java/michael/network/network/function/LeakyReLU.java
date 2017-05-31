/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package michael.network.network.function;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

/**
 *
 * @author michael
 */
public class LeakyReLU implements Function{
    @Override
    public DoubleMatrix x(DoubleMatrix m) {
        
        DoubleMatrix ret = m.dup();
        for(int i = 0;i<m.rows;i++){
            for(int j = 0;j<m.columns;j++){
                if(ret.get(i,j)<0){
                    ret.put(i, j, 0.01);
                }
            }
        }
        return ret;
    }

    @Override
    public DoubleMatrix dx(DoubleMatrix m) {
        DoubleMatrix ret = m.dup();
        for(int i = 0;i<m.rows;i++){
            for(int j = 0;j<m.columns;j++){
                if(ret.get(i,j)<=0){
                    ret.put(i, j, 0.01);
                }
                else{
                    ret.put(i, j, 1d);
                }
            }
        }
        return ret;
    }
}
