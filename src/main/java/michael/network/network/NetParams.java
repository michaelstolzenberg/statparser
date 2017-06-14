/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package michael.network.network;

import michael.network.network.function.Function;
import michael.network.network.function.ReLU;
import michael.network.network.function.Sigmoid;
import michael.network.network.function.Softmax;

/**
 *
 * @author michael
 */
public class NetParams {
    public int neurons;
    public int maxIter;
    public double hiddenBias;
    public double outBias;
    public double learningRate;
    public int batchSize;
    public Function hiddenFunction;
    public Function outFunction;
    
    public NetParams(){
        this.neurons = 5;
        this.maxIter = 10000;
        this.hiddenBias = 0.1;
        this.outBias = 0.1;
        this.learningRate = 0.1;
        this.batchSize = 1;
        this.hiddenFunction = new Sigmoid();
        this.outFunction = new Softmax();
    }
}
