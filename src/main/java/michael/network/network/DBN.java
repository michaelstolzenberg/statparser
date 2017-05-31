/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package michael.network.network;

import java.util.Arrays;

/**
 *
 * @author michael
 */
public class DBN {
        
    public DBN(){
    
    }
    
    public static void train(){
        // training data
        int[][] train_X = {
                {1, 1, 1, 0, 0, 0},
                {1, 0, 1, 0, 0, 0},
                {1, 1, 1, 0, 0, 0},
                {0, 0, 1, 1, 1, 0},
                {0, 0, 1, 0, 1, 0},
                {0, 0, 1, 1, 1, 0}
        };


        RBMParams params = new RBMParams();
        RBM rbm = new RBM(train_X, params);
        rbm.train();
        for (int[] x:rbm.out){
            System.out.println(Arrays.toString(x));
        }
        RBM rbm2 = new RBM(rbm.out, params);
        rbm2.train();
        for (int[] x:rbm2.out){
            System.out.println(Arrays.toString(x));
        }
        RBMParams params3 = new RBMParams();
        params3.nHidden = 12;
        RBM rbm3 = new RBM(rbm2.out, params3);
        rbm3.train();
        for (int[] x:rbm3.out){
            System.out.println(Arrays.toString(x));
        }
        
        

// test data
        int[][] test_X = {
                {1, 1, 0, 0, 0, 0},
                {0, 0, 0, 1, 1, 0}
        };
        
        
        
        int test_N = 2;


        double[][] reconstructed_X = new double[test_N][6];

        for(int i=0; i<test_N; i++) {
            rbm3.reconstruct(test_X[i], reconstructed_X[i]);
            for(int j=0; j<6; j++) {
                System.out.printf("%.5f ", reconstructed_X[i][j]);
            }
            System.out.println();
        }
    }
    public static void main(String[] args) {
        train();
    }  
}