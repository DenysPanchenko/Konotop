package konotop;
import java.util.regex.*;

public class Tokenizer {
   
    private Matcher mat;
    int curPosition = 0;
    
    void setText(String text){
        mat=unknown.matcher(text);
        curPosition=0;
    }
    
    Tokenizer(){
 
    }
    
    Tokenizer(String text){
        mat=unknown.matcher(text);
    }
    
    private final static Pattern end = Pattern.compile("\\G\\z");
    private final static Pattern space = Pattern.compile("\\G\\s+");
    private final static Pattern number = Pattern.compile("\\G(([+-]?0+\\.\\d+\\b)|(\\d+\\.?\\d*[Ee][+-]?\\d+\\b)|([+-]?[1-9]\\d*\\.\\d+\\b)|([+-]?[1-9]\\d*\\b)|(0[xX][A-Fa-f0-9]+\\b)|(0[0-7]*\\b))");
    private final static Pattern kwOrIdent = Pattern.compile("\\G[a-zA-Z_]\\w*");
    private final static Pattern separator = Pattern.compile("\\G[(){}\\[\\];,\\.]");
    private final static Pattern singleOp = Pattern.compile("\\G[=><!~?:+\\-*/&|\\^%]");
    private final static Pattern doubleOp = Pattern.compile("\\G((<=)|(>=)|(==)|(::)|(!=)|(\\|\\|)|"+                                                       "(\\&\\&)|(<<)|(>>)|(--)|(\\+\\+))");
    private final static Pattern assignOp = Pattern.compile("\\G((\\+=)|(-=)|(/=)|(\\*=)|(<<=)|"+                                                      "(>>=)|(\\|=)|(&=)|(\\^=))");
    private final static Pattern literal = Pattern.compile("\\G((\".*?([^\\\\]\"))|(\"\")|('[\\\\]?.?'))");
    private final static Pattern comment = Pattern.compile("\\G((//.*(?m)$)|(/\\*(?s).*?\\*/))");
    private final static Pattern directive = Pattern.compile("\\G#.*$");
    private final static Pattern unknown = Pattern.compile(".+?\\s+");
    
    public enum TokType{
        SPACE(space), UNKNOWN(unknown),
        NUMBER(number),KWORIDENT(kwOrIdent),SEPARATOR(separator),
        SINGLEOP(singleOp),DOUBLEOP(doubleOp),ASSIGNOP(assignOp),
        LITERAL(literal),COMMENT(comment),DIRECTIVE(directive),
        END(end);
        
        Pattern pat;
        TokType(Pattern p){
            pat=p;
        }
    }
    
    public class Token{
        String value;
        TokType type;
    }
    
    static TokType[] patterns = {TokType.END,TokType.NUMBER,TokType.KWORIDENT,TokType.LITERAL,TokType.COMMENT,TokType.ASSIGNOP,
                                TokType.DOUBLEOP,TokType.SINGLEOP,TokType.SEPARATOR,TokType.DIRECTIVE,TokType.UNKNOWN};
    
    Token getNextToken(){
        mat.usePattern(space).find();
        Token curToken = new Token();
        curToken.type = TokType.UNKNOWN;
        curToken.value = "";
        for(int i=0;i<patterns.length-1;i++){
            if(mat.usePattern(patterns[i].pat).find()){
                curToken.value = mat.group();
                curToken.type = patterns[i];
                curPosition = mat.end();
                return curToken;
            } 
       }
       mat.region(curPosition, mat.regionEnd());
       mat.usePattern(space).find();
       if(mat.usePattern(unknown).find()){
            curToken.value = mat.group();
            curToken.type = TokType.UNKNOWN;
        }
        return curToken;
    }
}
