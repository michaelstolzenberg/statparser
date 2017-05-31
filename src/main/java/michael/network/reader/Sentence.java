/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package michael.network.reader;

import java.util.List;

/**
 *
 * @author michael
 */
public class Sentence {
    public final List<Token> tokens;
    public Sentence(List<Token> tokens){
        this.tokens = tokens;
    }
}
