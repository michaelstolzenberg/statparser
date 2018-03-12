/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package michael.network.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jblas.DoubleMatrix;

/**
 *
 * @author michael
 */
public class Difficulty {
    private HashMap<Integer,Integer> map;
    private double total;
    private int strength;
    public Difficulty (){
        map = new HashMap();
        total = 0;
        strength = 10;
    }
    public void addY (DoubleMatrix results){
        for(DoubleMatrix d:results.rowsAsList()){
            int i = d.argmax();
            if(map.containsKey(i)){
                map.put(i,map.get(i)+1);
            }
            else{
                map.put(i,1);
            }
        }
        total = total + results.rows;
        //System.out.println(total);
        
    }
    public DoubleMatrix getDiff(DoubleMatrix results){
        for(int x =0;x<results.rows;x++){
            double scalar = (total/map.get(results.getRow(x).argmax()))/strength;
            //System.out.print(total + " "+ map.get(results.getRow(x).argmax()) + " " + scalar + " - ");
            results.putRow(x, results.getRow(x).mul(scalar));
        }
        //System.out.println();
        return results;
    }
}
