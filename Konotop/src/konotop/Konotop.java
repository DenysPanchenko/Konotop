/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package konotop;
import java.util.*;
/**
 *
 * @author denis
 */
public class Konotop {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Grammar gram = new Grammar();
        gram.SetBeginTerminal("A");
        ArrayList<String> N = new ArrayList<String>();
        Collections.addAll(N, "A","B","C","P","K","M");
        gram.SetNonTerminals(N);
        ArrayList<String> T = new ArrayList<String>();
        Collections.addAll(T, "a","b","c");
        
        ArrayList<String> k1 = new ArrayList<String>();
        Collections.addAll(k1, "B","C");
        ArrayList<String> k2 = new ArrayList<String>();
        Collections.addAll(k2, "B","P");
        ArrayList<String> k3 = new ArrayList<String>();
        Collections.addAll(k3, "A","K");
        ArrayList<String> k4 = new ArrayList<String>();
        Collections.addAll(k4, "M");
        
        Rule r1 = new Rule("A", k1);
        Rule r2 = new Rule("A", k2);
        Rule r3 = new Rule("B", k3);
        Rule r4 = new Rule("B", k4);
        HashSet<Rule> rules = new HashSet<Rule>();
        rules.add(r1);
        rules.add(r2);
        rules.add(r3);
        rules.add(r4);
        
        gram.SetRules(rules);
        HashSet<ArrayList<Rule>> res =  gram.FindMinLeftRecursiveDerivations();
        
    }
}
