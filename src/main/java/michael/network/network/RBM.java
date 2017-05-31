/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package michael.network.network;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author michael
 */
public class RBM {
    private final RBMParams params;
    private final double learningRate;
    private final int nEpochs;
    private final int k;
    private final int nExamples;
    private final int nVisible;
    private final int nHidden;
    private final int[][] examples;
    public int[][] out;
    private double[][] W;
    private double[] hChange;
    private double[] vChange;
    private final Random random;


    public RBM(int[][] examples,RBMParams params) {
        this.params = params;
        this.learningRate = params.learningRate;
        this.nEpochs = params.nEpochs;
        this.k = params.k;
        this.examples = examples;
        this.nExamples = examples.length;
        this.nVisible = params.nVisible;
        this.nHidden = params.nHidden;
        this.out = new int[nExamples][nHidden];
        this.random = new Random(params.seed);
        
        this.W = new double[nHidden][nVisible];
        double a = 1.0 / nVisible;
        for(int i=0; i<nHidden; i++) {
            for(int j=0; j<nVisible; j++) {
                this.W[i][j] = uniform(-a, a);
            }
        }

        this.hChange = new double[nHidden];
        for(int i=0; i<nHidden; i++){
            this.hChange[i] = 0;
        }
        
        this.vChange = new double[nVisible];
        for(int i=0; i<nVisible; i++){
            this.vChange[i] = 0;
        }
        
    }

    private double uniform(double min, double max) {
        return random.nextDouble() * (max - min) + min;
    }

    private int binomial(int n, double p) {
        if(p < 0 || p > 1) return 0;

        int c = 0;
        double r;

        for(int i=0; i<n; i++) {
            r = random.nextDouble();
            if (r < p) c++;
        }

        return c;
    }

    private double sigmoid(double x) {
        return 1. / (1. + Math.pow(Math.E, -x));
    }
    
    private int[] contrastive_divergence(int[] input, double lr, int k) {
        double[] ph_mean = new double[nHidden];
        int[] ph_sample = new int[nHidden];
        double[] nv_means = new double[nVisible];
        int[] nv_samples = new int[nVisible];
        double[] nh_means = new double[nHidden];
        int[] nh_samples = new int[nHidden];
		
		/* CD-k */
        sample_h_given_v(input, ph_mean, ph_sample);
        
        for(int step=0; step<k; step++) {
            if(step == 0) {
                gibbs_hvh(ph_sample, nv_means, nv_samples, nh_means, nh_samples);
            } else {
                gibbs_hvh(nh_samples, nv_means, nv_samples, nh_means, nh_samples);
            }
        }

        for(int i=0; i<nHidden; i++) {
            for(int j=0; j<nVisible; j++) {
                W[i][j] += lr * (ph_mean[i] * input[j] - nh_means[i] * nv_samples[j]) / nExamples;
            }
            hChange[i] += lr * (ph_sample[i] - nh_means[i]) / nExamples;
        }

        for(int i=0; i<nVisible; i++) {
            vChange[i] += lr * (input[i] - nv_samples[i]) / nExamples;
        }
        return nh_samples;
    }

    private void sample_h_given_v(int[] v0_sample, double[] mean, int[] sample) {
        for(int i=0; i<nHidden; i++) {
            mean[i] = propup(v0_sample, W[i], hChange[i]);
            sample[i] = binomial(1, mean[i]);
        }
    }

    private void sample_v_given_h(int[] h0_sample, double[] mean, int[] sample) {
        for(int i=0; i<nVisible; i++) {
            mean[i] = propdown(h0_sample, i, vChange[i]);
            sample[i] = binomial(1, mean[i]);
        }
    }

    private double propup(int[] v, double[] w, double b) {
        double pre_sigmoid_activation = 0.0;
        for(int j=0; j<nVisible; j++) {
            pre_sigmoid_activation += w[j] * v[j];
        }
        pre_sigmoid_activation += b;
        return sigmoid(pre_sigmoid_activation);
    }

    private double propdown(int[] h, int i, double b) {
        double pre_sigmoid_activation = 0.0;
        for(int j=0; j<nHidden; j++) {
            pre_sigmoid_activation += W[j][i] * h[j];
        }
        pre_sigmoid_activation += b;
        return sigmoid(pre_sigmoid_activation);
    }

    private void gibbs_hvh(int[] h0_sample, double[] nv_means, int[] nv_samples, double[] nh_means, int[] nh_samples) {
        sample_v_given_h(h0_sample, nv_means, nv_samples);
        sample_h_given_v(nv_samples, nh_means, nh_samples);
    }
    public void train(){
        for(int epoch=0; epoch<nEpochs; epoch++) {
            for(int i=0; i<nExamples; i++) {
                out[i]=contrastive_divergence(examples[i], learningRate, k);
            }
        }
    }
    
    public void reconstruct(int[] v, double[] reconstructed_v) {
        double[] h = new double[nHidden];
        double pre_sigmoid_activation;

        for(int i=0; i<nHidden; i++) {
            h[i] = propup(v, W[i], hChange[i]);
        }

        for(int i=0; i<nVisible; i++) {
            pre_sigmoid_activation = 0.0;
            for(int j=0; j<nHidden; j++) {
                pre_sigmoid_activation += W[j][i] * h[j];
            }
            pre_sigmoid_activation += vChange[i];

            reconstructed_v[i] = sigmoid(pre_sigmoid_activation);
        }
    }
}