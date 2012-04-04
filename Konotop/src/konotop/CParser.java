package konotop;

import java.util.ArrayList;
import java.util.HashSet;

public class CParser {
    //Shit-code here :-)
    private void _FormGrammar()  
    {
        m_grammar = new Grammar();
        //Begin terminal
        m_grammar.SetBeginTerminal("P");
        
        //Non terminals
        ArrayList<String> non_terminals = new ArrayList<String>();
        non_terminals.add("Prog"); non_terminals.add("Body"); non_terminals.add("Operator"); 
        non_terminals.add("Expression");
        m_grammar.SetNonTerminals(non_terminals);
        
        //Terminals
        ArrayList<String> terminals = new ArrayList<String>();
        terminals.add("int main()"); terminals.add("{"); terminals.add("}");
        terminals.add(";"); terminals.add("$"); terminals.add("var");
        terminals.add("="); terminals.add("number");
        m_grammar.SetTerminals(terminals);
        
        //Rules
        HashSet<Rule> rules = new HashSet<Rule>();
        ArrayList<String> right_part = new ArrayList<String>();
        right_part.add("int main()");
        right_part.add("Body");
        Rule rule = new Rule("P", right_part);
        rules.add(rule); right_part.clear();
        right_part.add("{"); right_part.add("Operator"); right_part.add("}");
        rule = new Rule("B", right_part);
        rules.add(rule); right_part.clear();
        right_part.add("Expression"); right_part.add(";"); right_part.add("Operator");
        rule = new Rule("O", right_part);
        rules.add(rule); right_part.clear();
        right_part.add("$");
        rule = new Rule("O", right_part);
        rules.add(rule); right_part.clear();
        right_part.add("var"); right_part.add("="); right_part.add("number");
        rules.add(rule);
        m_grammar.SetRules(rules);
    }
    private Grammar m_grammar;
    
}