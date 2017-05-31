/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package michael.network.network;

/**
 *
 * @author michael
 */
public class RBMParams {
    public int nHidden;
    public int nVisible;
    public int k;
    public int nEpochs;
    public double learningRate;
    public int seed;
    
    public RBMParams(){
        this.nHidden = 6;
        this.nVisible = 6;
        this.k = 1;
        this.nEpochs = 30;
        this.learningRate = 0.1;
        this.seed = 123;
    }
}
