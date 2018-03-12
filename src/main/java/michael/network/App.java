package michael.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import michael.network.features.DefaultGenerator;
import michael.network.guide.Guide;
import michael.network.guide.NetGuide;
import michael.network.guide.StackProjectiveOracle;
import michael.network.reader.CharacterMap;
import michael.network.network.Net;
import michael.network.parser.Dependency;
import michael.network.parser.GreedyParser;
import michael.network.parser.GreedyTrainer;
import michael.network.parser.Parser;
import michael.network.parser.system.stackprojective.StackProjectiveTransitionSystem;
import michael.network.reader.CONLLUReader;
import michael.network.reader.GloVeReader;
import michael.network.reader.Sentence;
import michael.network.reader.Util;
import org.jblas.DoubleMatrix;


/**
 *
 * @author michael
 */
public class App {
    public static void main (String [] args)throws IOException{
        //String trainFile = "en-ud-dev.conllu";
        //CONLLUReader reader = new CONLLUReader(trainFile);
        //try {
        //    //Sentence sentence;
        //    while (reader.readSentence() != null) {
        //        
        //    }    
        //} 
        //finally {
        //    reader.close();
       // }
        //reader.characterMap.printAllChars();
        //System.out.println(reader.characterMap.getVector('r'));
        //System.out.println(reader.characterMap.getChar(reader.characterMap.getVector('r')));
        Boolean v = false;
        String outFile = "out.conllu";
        String trainFile = "en-ud-dev.conllu";
        String testFile = "en-ud-test.conllu";
        String gloveFile = "glove.txt";

////////////////////////
///read/embeddings//////
////////////////////////
        if(v)System.out.println("reading pretrained embeddings..");
        
        GloVeReader gr = new GloVeReader(gloveFile);
        //gr.read();
        
        if(v)System.out.println("done..");
////////////////////////
////read/data///////////
////////////////////////        
        if(v)System.out.println("reading training data..");
        
        CONLLUReader reader = new CONLLUReader(trainFile);
        
        if(v)System.out.println("done..");
////////////////////////
/////create/examples////
////////////////////////        
        if(v)System.out.println("creating examples..");
        
        GreedyTrainer trainer = new GreedyTrainer(new StackProjectiveTransitionSystem(), new DefaultGenerator(),gr);
        try {
            Sentence sentence;
            while ((sentence = reader.readSentence()) != null) {
                Set<Dependency> goldDependencies = Util.sentenceToDependencies(sentence);
                Guide guide = new StackProjectiveOracle(goldDependencies);
                trainer.parse(Util.sentenceToTokens(sentence), Util.sentenceToTags(sentence), guide);
            }    
        } 
        finally {
            reader.close();
        }
        DoubleMatrix examples = trainer.sparseToDense();
        DoubleMatrix labels = trainer.sparseToDenseLabel();
        
        if(v)System.out.println("done..");
////////////////////////
////////initiate////////
////////////////////////        
        if(v)System.out.println("initiating network..");
        
        Net net = new Net(examples,labels);
        
        if(v)System.out.println("done..");
////////////////////////
////////training////////
////////////////////////
        if(v)System.out.println("training..");
        
//test
        for(int i = 0;i<25;i++){
        net.train();
        
        if(v)System.out.println("done..");
////////////////////////
////////parsing/////////
////////////////////////
        if(v)System.out.println("parsing..");
                 
        NetGuide netGuide = new NetGuide(net,new DefaultGenerator(),trainer.featureVectorizer,trainer.labelNumberer,gr);
        Parser parser = new GreedyParser(new StackProjectiveTransitionSystem(), netGuide);
        
        CONLLUReader testReader = new CONLLUReader(testFile);
        StringBuilder sb = new StringBuilder();
        PrintWriter writer = new PrintWriter(outFile, "UTF-8");
        int nTotal = 0;
        int nCorrect = 0;
        int nSents = 0;
        int LAS = 0;
        int UAS = 0;
        HashMap<String,List<Integer>> eval = new HashMap<>();   
            try {
            
            Sentence sentence;
            List<Dependency> l = new ArrayList<>();
            while ((sentence = testReader.readSentence()) != null) {
                Set<Dependency> goldDependencies = Util.sentenceToDependencies(sentence);
                Set<Dependency> foundDependencies = parser.parse(Util.sentenceToTokens(sentence), Util.sentenceToTags(sentence));
                l.clear();
                l.addAll(foundDependencies);
                Collections.sort(l, new Comparator<Dependency>() {
                    public int compare(Dependency o1, Dependency o2) {
                        return o1.dependentIndex()-o2.dependentIndex();
                    }
                });
                for(Dependency d:l){
                    sb.append(d.dependentIndex()).append("\t_\t_\t_\t_\t_\t").append(d.headIndex()).append("\t").append(d.relation()).append("\t_\t_\n");
                }
                sb.append("\n");
                Set<Dependency>d1=new HashSet<>(goldDependencies);
                Set<Dependency>d2=new HashSet<>(d1);
                d1.retainAll(foundDependencies);
                d2.removeAll(foundDependencies);
                for(Dependency d:d1){
                    if(eval.containsKey(d.relation())){
                        List<Integer> copy = new ArrayList<>(eval.get(d.relation()));
                        copy.set(0,copy.get(0)+1);
                        eval.put(d.relation(),copy);   
                    }
                    else{
                        eval.put(d.relation(),Arrays.asList(1,0));
                    }
                }
                for(Dependency d:d2){
                    if(eval.containsKey(d.relation())){
                        List<Integer> copy = new ArrayList<>(eval.get(d.relation()));
                        copy.set(1,copy.get(1)+1);
                        eval.put(d.relation(),copy); 
                    }
                    else{
                        eval.put(d.relation(),Arrays.asList(1,0));
                    }
                }
                
                LAS += Util.LAS(goldDependencies, foundDependencies);
                UAS += Util.UAS(goldDependencies, foundDependencies);
                
                //foundDependencies.retainAll(goldDependencies);
                
                nTotal += goldDependencies.size();
                //nCorrect += foundDependencies.size();
                
                // Some progress report...
                //if (++nSents % 100 == 0) {
                //    System.out.printf(" %d%n", nSents);
                //}
            }
        } finally {
            
            writer.print(sb);
            writer.close();
            testReader.close();
        }

        // Print percentage of correct attachments.
        //System.out.printf("LAS: %.4f%n",(double) LAS / nTotal * 100);
        //System.out.printf("UAS: %.4f%n",(double) UAS / nTotal * 100);
        System.out.printf("LAS: %.4f\tUAS: %.4f%n",(double) LAS / nTotal * 100,(double) UAS / nTotal * 100);
        //System.out.printf("%.4f%n",(double) LAS / nTotal * 100);
        //System.out.println(eval);
//test remove bracket
        }
    
    }
}