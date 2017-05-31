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
public class Batch {
        public final DoubleMatrix examples;
        public final DoubleMatrix results;
        public Batch(DoubleMatrix examples,DoubleMatrix results){
            this.examples = examples;
            this.results = results;
        }
    }
