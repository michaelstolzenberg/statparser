/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package michael.network.network;

import michael.network.network.function.ELU;
import michael.network.network.function.Function;
import michael.network.network.function.LeakyReLU;
import michael.network.network.function.ReLU;
import michael.network.network.function.Sigmoid;
import michael.network.network.function.Softmax;
import michael.network.network.optimizer.AdaDelta;
import michael.network.network.optimizer.Optimizer;
import michael.network.network.optimizer.Vanilla;

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
    public Optimizer hiddenOptimizer;
    public Optimizer outOptimizer;
    public Optimizer hiddenBiasOptimizer;
    public Optimizer outBiasOptimizer;
    //public double inputDropoutProbability;
    public double hiddenDropoutProbability;
    
    public NetParams(){
        this.neurons = 50;//30
        this.maxIter = 10;//10
        this.hiddenBias = 0;
        this.outBias = 0;
        this.learningRate = 0.01;
        this.batchSize = 1000;//1000
        this.hiddenFunction = new ELU();
        this.outFunction = new Softmax();
        this.hiddenOptimizer = new AdaDelta();
        this.outOptimizer = new AdaDelta();
        this.hiddenBiasOptimizer = new AdaDelta();
        this.outBiasOptimizer = new AdaDelta();
        //this.inputDropoutProbability = 0.2;
        this.hiddenDropoutProbability = 0;
    }
}
