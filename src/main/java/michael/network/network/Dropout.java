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
public class Dropout {
    private final double hiddenProbability;
    private final double inputProbability;
    private final int hiddenSize;
    private final int inputSize;
    public DoubleMatrix hiddenMask;
    public DoubleMatrix inputMask;
        
    public Dropout(double hiddenProbability, int hiddenSize, double inputProbability, int inputSize){
        this.hiddenProbability = hiddenProbability;
        this.inputProbability = inputProbability;
        this.hiddenSize = hiddenSize;
        this.inputSize = inputSize;
    }
    public void createMasks(){
        hiddenMask = createDropoutMask(hiddenProbability,hiddenSize);
        inputMask = createDropoutMask(inputProbability,inputSize);
    }
    private DoubleMatrix createDropoutMask(double prob,int size){
        DoubleMatrix m = DoubleMatrix.rand(size).transpose();
        for(int i =0;i<size;i++){
            if(m.get(i)<=prob){
                m.put(i,0d);
            }
            else{
                m.put(i,1d);
            }
        }
    return m;
    }
}