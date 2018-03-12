/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package michael.network.network;

import org.jblas.DoubleMatrix;

/**
 *
 * @author michael
 */
public interface Layer {
    public void forward();
    public void back();
}