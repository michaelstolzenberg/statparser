package michael.network.reader;

import java.util.List;

public class Token {
    public final int id;
    public final String form;
    public final String lemma;
    public final String uPosTag;
    public final String xPosTag;
    public final List<String> feats;
    public final int head;
    public final String depRel;
    public final String deps;
    public final String misc;
    
    public Token(int id,String form,String lemma,String uPosTag,String xPosTag,List<String> feats,int head,String depRel,String deps,String misc){
        this.id = id;
        this.form = form;
        this.lemma = lemma;
        this.uPosTag = uPosTag;
        this.xPosTag = xPosTag;
        this.feats = feats;
        this.head = head;
        this.depRel = depRel;
        this.deps = deps;
        this.misc = misc;
    }
}