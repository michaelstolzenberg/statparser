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
        private final DoubleMatrix allExamples;
        private final DoubleMatrix allResults;
        private final DoubleMatrix currentExamples;
        private final DoubleMatrix currentResults;
        private int index;
        private final int batchSize;
        
        public Batcher(DoubleMatrix allExamples,DoubleMatrix allResults,int batchSize){
            this.allExamples = allExamples;
            this.allResults = allResults;
            this.currentExamples = new DoubleMatrix(batchSize,allExamples.columns);
            this.currentResults = new DoubleMatrix(batchSize,allResults.columns);
            this.index = 0;
            this.batchSize = batchSize;
        }
        public Batch nextBatch(){
            if(hasNext()){
                if(batchSize+index<allExamples.rows){ 
                    for(int i=0;i<batchSize;i++){
                        currentExamples.putRow(i,allExamples.getRow(i+index));
                        currentResults.putRow(i,allResults.getRow(i+index));
                    }
                index+=batchSize;
//v
//                System.out.println("remaining: "+(allExamples.rows-index));
                return new Batch(currentExamples,currentResults);  
                }
//drop last batch                
                //else{
                //    for(int i=0;i<allExamples.rows -index-1;i++){
                //        currentExamples.putRow(i,allExamples.getRow(i+index));
                //        currentResults.putRow(i,allResults.getRow(i+index));
                //    }
                //}  
                index+=batchSize;
            }
            return null;
            
        }
        public Boolean hasNext(){
            return(index+batchSize<allExamples.rows);
        }
    }