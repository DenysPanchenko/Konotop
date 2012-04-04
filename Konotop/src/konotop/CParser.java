package konotop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CParser {
    
    public CParser(String programText){
        _FormGrammar();
        _CreateMapping();
        _FormTable();
        m_program = programText;
        m_tokenizer = new Tokenizer(m_program);
    }
    
    private void _CreateMapping(){
        m_mapping = new HashMap<String,HashSet<Rule>>();
        HashSet<Rule> cur_rules = m_grammar.GetRules();
        for(Rule rule : cur_rules){
            String leftPart = rule.GetLeftPart();
            if(m_mapping.containsKey(leftPart)){
                m_mapping.get(leftPart).add(rule);
            }
            else{
                HashSet<Rule> cur_set = new HashSet<Rule>();
                cur_set.add(rule);
                m_mapping.put(leftPart, cur_set);
            }
        }
    }
    
    private void _FormTable(){
        m_table = new HashMap<Rule,HashSet<ArrayList<String>>>();
        HashSet<Rule> cur_rules = m_grammar.GetRules();
        for(Rule rule : cur_rules){
            HashSet<ArrayList<String>> set = m_grammar.RuleContext(rule);
            m_table.put(rule, set);
        }
    }

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
    private HashMap<String,HashSet<Rule>> m_mapping;
    private HashMap<Rule,HashSet<ArrayList<String>>> m_table;
    private String m_program;
    private Tokenizer m_tokenizer;
}