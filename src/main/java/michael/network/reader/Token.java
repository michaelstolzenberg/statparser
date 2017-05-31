package michael.network.reader;

public class Token {
    public final int tokenId;
    public final String form;
    public final String lemma;
    public final String uTag;
    public final String tag;
    public final String features;//make List
    public final int head;
    public final String headRel;
    
    public Token(int tokenId,String form,String lemma,String uTag,String tag,String features,int head,String headRel){
        this.tokenId = tokenId;
        this.form = form;
        this.lemma = lemma;
        this.uTag = uTag;
        this.tag = tag;
        this.features = features;
        this.head = head;
        this.headRel = headRel;
    }
}