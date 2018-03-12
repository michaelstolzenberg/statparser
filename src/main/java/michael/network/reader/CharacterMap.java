/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package michael.network.reader;

import java.util.HashMap;
import java.util.Map;
import org.jblas.DoubleMatrix;

/**
 *
 * @author michael
 */
public class CharacterMap {
    private final Map<Character, Integer> charMap;
    private final Map<Integer, Character> intMap;
       
    public CharacterMap(){
        charMap = new HashMap<>();
        intMap = new HashMap<>();
    }
    
    public void put(char c){
        if(!charMap.containsKey(c)){
            int i = charMap.size();
            charMap.put(c,i);
            intMap.put(i,c);
        }
    }
    
    public void put(String s){
        for(int x = 0;x<s.length();x++){
            char c = s.charAt(x);
            if(!charMap.containsKey(c)){
                int i = charMap.size();
                charMap.put(c,i);
                intMap.put(i,c);
            }
        }
    }
    
    public DoubleMatrix getVector(char c){
        DoubleMatrix ret = DoubleMatrix.zeros(charMap.size());
        ret.put(charMap.get(c), 1d);
        return ret;
    }
    
    public char getChar(DoubleMatrix m){
        char ret = intMap.get(m.argmax());
        return ret;
    }
    
    public int size(){
        return charMap.size();
    }
    
    public void printAllChars(){
        System.out.println(charMap.keySet());
    }
}