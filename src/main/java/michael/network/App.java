package michael.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Set;
import michael.network.features.DefaultGenerator;
import michael.network.guide.Guide;
import michael.network.guide.NetGuide;
import michael.network.guide.NoLabelOracle;
import michael.network.network.function.LeakyReLU;
import michael.network.network.Net;
import michael.network.network.function.ReLU;
import michael.network.network.function.Sigmoid;
import michael.network.parser.Dependency;
import michael.network.parser.GreedyParser;
import michael.network.parser.GreedyTrainer;
import michael.network.parser.Parser;
import michael.network.parser.system.stackprojective.StackProjectiveTransitionSystem;
import michael.network.reader.CONLLUReader;
import michael.network.reader.Sentence;
import michael.network.reader.Util;
import org.jblas.DoubleMatrix;


/**
 *
 * @author michael
 */
public class App {
    public static void main (String [] args)throws IOException{
        
        String outFile = "out.conllu";
        String trainFile = "train.conllu";
        String testFile = "test.conllu";
        
        CONLLUReader reader = new CONLLUReader(trainFile);
        GreedyTrainer trainer = new GreedyTrainer(new StackProjectiveTransitionSystem(), new DefaultGenerator());
        
        try {
            Sentence sentence;
            while ((sentence = reader.readSentence()) != null) {
                Set<Dependency> goldDependencies = Util.sentenceToDependencies(sentence);
                Guide guide = new NoLabelOracle(goldDependencies);
                trainer.parse(Util.sentenceToTokens(sentence), Util.sentenceToTags(sentence), guide);
            }    
        } 
        finally {
            reader.close();
        }
        
        DoubleMatrix examples = new DoubleMatrix(trainer.sparseToDense());
        DoubleMatrix labels = new DoubleMatrix(trainer.sparseToDenseLabel());
        
        
     
        
        Net net = new Net(examples,labels);
        net.train();
        
              
        NetGuide netGuide = new NetGuide(net,new DefaultGenerator(),trainer.featureVectorizer,trainer.labelNumberer);
        Parser parser = new GreedyParser(new StackProjectiveTransitionSystem(), netGuide);
        
        CONLLUReader testReader = new CONLLUReader(testFile);
        StringBuilder sb = new StringBuilder();
        PrintWriter writer = new PrintWriter(outFile, "UTF-8");
        int nTotal = 0;
        int nCorrect = 0;
        int nSents = 0;
        
            try {
            
            Sentence sentence;
            while ((sentence = testReader.readSentence()) != null) {
                Set<Dependency> goldDependencies = Util.sentenceToDependencies(sentence);
                Set<Dependency> foundDependencies = parser.parse(Util.sentenceToTokens(sentence), Util.sentenceToTags(sentence));
                
                for(Dependency d:foundDependencies){
                    sb.append(d.dependentIndex()).append("\t_\t_\t_\t_\t_\t").append(d.headIndex()).append("\t").append(d.relation()).append("\t_\t_\n");
                }
                sb.append("\n");
                
                foundDependencies.retainAll(goldDependencies);
                nTotal += goldDependencies.size();
                nCorrect += foundDependencies.size();

                // Some progress report...
                if (++nSents % 100 == 0) {
                    System.err.printf(" %d%n", nSents);
                }
            }
        } finally {
            writer.print(sb);
            writer.close();
            testReader.close();
        }

        // Print percentage of correct attachments.
        System.err.printf("%nAttachment score: %.4f%n", (double) nCorrect / nTotal * 100);
        
    }
}
