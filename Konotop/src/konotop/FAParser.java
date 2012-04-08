package konotop;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

public class FAParser{
    private Grammar gram;
    private File inputFile;
    private ArrayList<Integer> states;
    private Integer curState;
    
    private Pair<String,Integer> getNonTerminal(String str, int pos) throws Exception {
        int i;
        StringBuilder nonTerminal = new StringBuilder();
        for(i = pos; i < str.length(); i++){
            if(str.charAt(i) != '\\') {
                if(str.charAt(i) != '#' && str.charAt(i) != '!'){
                    if(!Character.isSpaceChar(str.charAt(i)))
                        nonTerminal.append(str.charAt(i));
                    else
                        throw new Exception("Parsing error! There are no spaces in non-terminal");
                }
                else
                if(str.charAt(i) == '!')
                    throw new Exception("Parsing error! Wrong symbol in non-terminal: " + str.charAt(i));
                else
                if(str.charAt(i) == '#') {
                    return new Pair<String, Integer>(nonTerminal.toString(), new Integer(i));
                }
            }
            else {
                if(str.length() > i && str.charAt(i + 1) == '#'){
                    nonTerminal.append('#');
                    i++;
                }
                else
                if(str.length() > i && str.charAt(i + 1) == '!'){
                    nonTerminal.append('!');
                    i++;
                }
                else
                if(str.length() > i && str.charAt(i + 1) == '\\'){
                    nonTerminal.append('\\');
                    i++;
                }
                else
                    throw new Exception("Parsing error! Wrong symbol in non-terminal: " + str.charAt(i));
            } 
        }
        return new Pair<String, Integer>(nonTerminal.toString(), new Integer(i));
    }
    
    private Pair<String,Integer> getTerminal(String str, int pos) throws Exception {
        int i;
        StringBuilder terminal = new StringBuilder();
        for(i = pos; i < str.length(); i++){
            if(str.charAt(i) != '\\') {
                if((str.charAt(i) == '#' || str.charAt(i) == '!') || (str.charAt(i) == '\n' || Character.isSpaceChar(str.charAt(i)))){
                    return new Pair<String, Integer>(terminal.toString(), new Integer(i - 1));
                }
                else{
                    terminal.append(str.charAt(i));
                }
            }
            else {
                if(str.length() > i && str.charAt(i + 1) == '#'){
                    terminal.append('#');
                    i++;
                }
                else
                if(str.length() > i && str.charAt(i + 1) == '!'){
                    terminal.append('!');
                    i++;
                }
                else
                if(str.length() > i && str.charAt(i + 1) == '\\'){
                    terminal.append('\\');
                    i++;
                }
                else
                    throw new Exception("Parsing error! Wrong symbol in terminal: " + str.charAt(i));
            } 
        }
        return new Pair<String, Integer>(terminal.toString(), new Integer(i));
    }
    
    public void setInputFile(File in) throws Exception{
        if(in.isFile() && in.canRead())
            inputFile = new File(in.getAbsolutePath());
        else
            throw new Exception("Error! Input file cannot be read");
    }
    
    public String getInputFile(){
        return inputFile.getAbsolutePath();
    }
    
    private String removeExtraSpaces(String str){
        int i,j;
        StringBuilder sb = new StringBuilder(str);       
        for(i = 0; i < sb.length(); i++){
            if(Character.isSpaceChar(sb.charAt(i))){
                j = i + 1;
                while(j < sb.length() && Character.isSpaceChar(sb.charAt(j))){
                    sb.deleteCharAt(j);
                }
            }
        }
        if(sb.length() > 0)
            if(Character.isSpaceChar(sb.charAt(sb.length() - 1)))
                sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    
    public void parseInputString(String str) throws Exception{
        try{
            ArrayList<String>  term = new ArrayList<String>(gram.GetTerminals());
            ArrayList<String> nTerm = new ArrayList<String>(gram.GetNonTerminals());
            HashSet<Rule> nRules = new HashSet<Rule>(gram.GetRules());
            if(str.charAt(0) == '#'){
                  curState = 0;
                        for(Rule r : parseLine(str)){
                            if(!nRules.contains(r))
                                nRules.add(r);
                            for(String nt : r.GetRightNonTerminals()){
                                if(!nTerm.contains(nt)){
                                    nTerm.add(nt);
                                }
                            }
                    
                            if(!nTerm.contains(r.GetLeftPart())){
                                if(gram.GetBeginNonTerminal() == null)
                                        gram.SetBeginTerminal(r.GetLeftPart());
                                nTerm.add(r.GetLeftPart());
                            }
                        
                            for(String t : r.GetRightTerminals()){
                                if(!term.contains(t))
                                    term.add(t);
                            }
                        }
                        gram.SetRules(nRules);
                        gram.SetNonTerminals(nTerm);
                        gram.SetTerminals(term);
                    }
            else
                throw new Exception("Parse error! Wrong begining of line");
        }
        catch(Exception e){
            throw new Exception(e);
        }
    }
    
    private HashSet<Rule> parseLine(String str) throws Exception{ 
        ArrayList<String>  terminals = new ArrayList<String>();
        ArrayList<String> nTerminals = new ArrayList<String>();
        
        String leftPart = new String();
        ArrayList<String> rightPart = new ArrayList<String>();
        HashSet<Rule> rules = new HashSet<Rule>();
        try{           
            String left;
            for(int i = 0; i < str.length(); i++){
            switch(curState){
                case 0:
                    if(str.charAt(i) == '#'){
                        leftPart = getNonTerminal(str,i + 1).getFirst();
                        i = getNonTerminal(str, i + 1).getSecond();
                        curState = 1;
                    }
                    break;
                case 1:
                    if(Character.isSpaceChar(str.charAt(i)))
                        continue;
                    else{
                        if(str.length() > i + 1)
                            if((str.charAt(i) == ':' && str.charAt(i + 1) == ':') && (str.charAt(i + 2) == '=')){
                                i += 2;
                                curState = 2;
                            }
                        else
                                throw new Exception("Parsing error! Cannot find identifier ::=");
                    }
                    break;
                case 2:
                    if(Character.isSpaceChar(str.charAt(i)))
                        continue;
                    else{
                        if(str.charAt(i) == '#'){
                            left = getNonTerminal(str, i + 1).getFirst();
                            if(left.length() > 0){
                                i = getNonTerminal(str, i + 1).getSecond();
                                rightPart.add(left);
                                nTerminals.add(left);
                                curState = 3;
                            }
                            else 
                                throw new Exception("Parsing error! Empty non-terminal");
                        }
                        else{
                            left = getTerminal(str, i).getFirst();
                            if(left.length() > 0){
                                i = getTerminal(str, i).getSecond();
                                rightPart.add(left);
                                terminals.add(left);
                                curState = 3;
                            }
                            else
                                throw new Exception("Parsing error! Empty terminal");
                        }
                    }
                    break;
                case 3:
                    if(Character.isSpaceChar(str.charAt(i)))
                        continue;
                    else{
                        if(str.charAt(i) == '!'){
                            if(!rightPart.isEmpty() && !leftPart.isEmpty()){
                                Rule r = new Rule();
                                r.SetRightNonTerminals(nTerminals);
                                r.SetRightTerminals(terminals);
                                r.SetLeftPart(new String(leftPart));
                                r.SetRightPart(new ArrayList<String>(rightPart));
                                rules.add(r);
                                nTerminals.clear();
                                terminals.clear();
                                rightPart.clear();
                            }
                            else
                                throw new Exception("Parsing error! Empty left part or right part");
                            rightPart.clear();
                            curState = 2;
                        }
                        else
                        if(str.charAt(i) == '#'){
                            left = getNonTerminal(str, i + 1).getFirst();
                            if(left.length() > 0){
                                i = getNonTerminal(str, i + 1).getSecond();
                                rightPart.add(left);
                                nTerminals.add(left);
                            }
                            else 
                                throw new Exception("Parsing error! Empty non-terminal");
                        }
                        else{
                            left = getTerminal(str, i).getFirst();
                            if(left.length() > 0){
                                i = getTerminal(str, i).getSecond();
                                rightPart.add(left);
                                terminals.add(left);
                            }
                            else 
                                throw new Exception("Parsing error! Empty terminal");
                        }
                    }
                    break;
                }
            }
            Rule r = new Rule();
            r.SetRightNonTerminals(nTerminals);
            r.SetRightTerminals(terminals);
            r.SetLeftPart(leftPart);
            r.SetRightPart(rightPart);
            rules.add(r);            
        }
        catch(Exception e){
            throw new Exception(e);
        }
        return rules;
    }
    
    public void parseFile() throws Exception{
        try{
            FileInputStream fstream = new FileInputStream(inputFile.getAbsolutePath());
            DataInputStream dstream = new DataInputStream(fstream);
            BufferedReader in = new BufferedReader(new InputStreamReader(dstream));

            String str;

            ArrayList<StringBuilder> fileCont = new ArrayList<StringBuilder>();
            String clrStr;
            while((str = in.readLine()) != null){
                clrStr = removeExtraSpaces(str);
                if(!clrStr.isEmpty()){
                    if((clrStr.charAt(0) == ' ' || clrStr.charAt(0) == '\t') || str.charAt(0) == '!'){
                        if(fileCont.size() > 0){
                            fileCont.get(fileCont.size() - 1).append(clrStr);
                            continue;
                        }
                        else
                            throw new Exception("Parse error! Wrong begining of line");
                    }
                    fileCont.add(new StringBuilder(clrStr));
                }
            }            

            ArrayList<String> nTerm = new ArrayList<String>(gram.GetNonTerminals());
            ArrayList<String> term = new ArrayList<String>(gram.GetTerminals());
            HashSet<Rule> ru = new HashSet<Rule>(gram.GetRules());
            
            curState = 0;
            if(fileCont.size() > 0){
                int i = 0;
                for(Rule r : parseLine(fileCont.get(0).toString())){
                    if(i == 0)
                        gram.SetBeginTerminal(r.GetLeftPart());
                    else
                        break;
                }
            }
                
            
            for(StringBuilder sb : fileCont){
                curState = 0;
                for(Rule r : parseLine(sb.toString())){
                    if(!ru.contains(r))
                        ru.add(r);
                    for(String nt : r.GetRightNonTerminals()){
                        if(!nTerm.contains(nt))
                            nTerm.add(nt);
                    }
                    if(!nTerm.contains(r.GetLeftPart()))
                        nTerm.add(r.GetLeftPart());
                    for(String t : r.GetRightTerminals()){
                        if(!term.contains(t))
                            term.add(t);
                    }
                }
            }
            gram.SetRules(ru);
            gram.SetNonTerminals(nTerm);
            gram.SetTerminals(term);
        }
        catch(Exception e){
            throw new Exception(e);
        }
    }
    
    public Grammar getGrammar(){
        return gram;
    }
    
    public void clear(){
        gram = new Grammar();
        curState = 0;
    }
    
    public FAParser(){
        gram = new Grammar();
        states = new ArrayList<Integer>();
        states.add(0);
        states.add(1);
        states.add(2);
        states.add(3);
    }
}
