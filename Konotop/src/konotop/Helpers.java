package konotop;

import java.util.ArrayList;

public class Helpers {
    static ArrayList<String> Union(ArrayList<String> i_set1, ArrayList<String> i_set2)
    {
        ArrayList<String> union = new ArrayList<String>();
        union.addAll(i_set1);
        for(String str : i_set2)
        {
            if(!i_set1.contains(str))
                union.add(str);
        }
        return union;
    }
    
    static ArrayList<String> Difference(ArrayList<String> i_set1, ArrayList<String> i_set2)
    {
        ArrayList<String> difference = new ArrayList<String>();
        for(String str : i_set1)
        {
            if(!i_set2.contains(str))
            {
                difference.add(str);
            }
        }
        return difference;
    }
    
    static Pair<String,Integer> getNonTerminal(String str, int pos) throws Exception {
        int i;
        StringBuilder nonTerminal = new StringBuilder();
        for(i = pos; i < str.length(); i++){
            if(str.charAt(i) != '\\') {
                if(str.charAt(i) != '#' && str.charAt(i) != '!')
                    nonTerminal.append(str.charAt(i));
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
                    throw new Exception("Parsing error! Wrong symbol in axiom: " + str.charAt(i));
            } 
        }
        return new Pair<String, Integer>(nonTerminal.toString(), new Integer(i));
    }
    
    static Pair<String,Integer> getTerminal(String str, int pos) throws Exception {
        int i;
        StringBuilder terminal = new StringBuilder();
        for(i = pos; i < str.length(); i++){
            if(str.charAt(i) != '\\') {
                if((str.charAt(i) == '#' || str.charAt(i) == '!') || (str.charAt(i) == '\n')){
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
                    throw new Exception("Parsing error! Wrong symbol in axiom: " + str.charAt(i));
            } 
        }
        return new Pair<String, Integer>(terminal.toString(), new Integer(i));
    }
}
