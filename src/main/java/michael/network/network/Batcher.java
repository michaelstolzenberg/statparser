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
public class Batcher {
        public final DoubleMatrix allExamples;
        public final DoubleMatrix allResults;
        private DoubleMatrix remainingExamples;
        private DoubleMatrix remainingResults;
        private final int batchSize;
        
        public Batcher(DoubleMatrix allExamples,DoubleMatrix allResults,int batchSize){
            this.allExamples = allExamples;
            this.allResults = allResults;
            this.remainingExamples = allExamples;
            this.remainingResults = allResults;
            this.batchSize = batchSize;
        }
        public Batch nextBatch(){
            if(hasNext()){
                int nex = remainingExamples.columns;
                int nre = remainingResults.columns;
                int total = remainingExamples.rows;
                int size = batchSize;
                if(total<size){
                    size = total;
                }
                DoubleMatrix cex = new DoubleMatrix(size,nex);
                DoubleMatrix rex = new DoubleMatrix(total-size,nex);
                DoubleMatrix cre = new DoubleMatrix(size,nre);
                DoubleMatrix rre = new DoubleMatrix(total-size,nre);
//print remaining examples      
                
                //System.out.println("Remaining examples: "+total);
                
                for(int i=0;i<total;i++){
                    if(i<size){
                        cex.putRow(i,remainingExamples.getRow(i));
                        cre.putRow(i,remainingResults.getRow(i));
                    }
                    else{
                        rex.putRow(i-size,remainingExamples.getRow(i));
                        rre.putRow(i-size,remainingResults.getRow(i));
                    }
                }
                remainingExamples=rex;
                remainingResults=rre;
                return new Batch(cex,cre);
            }
            return null;
        }
        public Boolean hasNext(){
            return(remainingExamples.rows>0);
        }
    }