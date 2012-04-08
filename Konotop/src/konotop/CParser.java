package konotop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.io.File;

public class CParser {
    
    public CParser(String programText, Grammar grammar){
        m_grammar = grammar;
        _FormKeyWords();
        _CreateMapping();
        _FormTable();
        m_tokenizer = new Tokenizer(programText);
    }
    //todo: exclude forming grammar
    
    private void _replaceToken(){
        if(m_token.type == Tokenizer.TokType.NUMBER)
            m_token.value = numer_terminal;
        else if(m_token.type == Tokenizer.TokType.KWORIDENT 
                && !(m_key_words.contains(m_token.value)))
            m_token.value = variable_terminal;
    }
    
    public boolean Parse()
    {
        m_token = m_tokenizer.getNextToken();
        _replaceToken();
        return _ParseNonTerminal(m_grammar.GetBeginTerminal());
    }
    
    void SetGrammar(Grammar i_grammar)
    {
        m_grammar = i_grammar;
        _CreateMapping();
        _FormTable();
    }
           
    void SetProgramText(String i_program_text)
    {
        m_tokenizer.setText(i_program_text);
    }
    
    private Rule _NextRule(String nonterm){
        HashSet<Rule> prog_rules = m_mapping.get(nonterm);
        Rule rule = new Rule();
        for(Rule cur_rule : prog_rules)
        {
            HashSet<String> cur_set = m_table.get(cur_rule);
            if(cur_set.contains(m_token.value))
            {
                rule = cur_rule;
                return rule;
            }
        }
        for(Rule ext_rule : prog_rules){
            if(ext_rule.IsEpsilonRule())
            {
                rule = ext_rule;
                return rule;
            }
        }
        return rule;
    } 
    
    private boolean _ParseRightpart(Rule rule){
        ArrayList<String> right_part = rule.GetRightPart();
        for(String symbol : right_part)
        {
            if(m_grammar.IsTerminal(symbol)){
                if(symbol.equals(Grammar.epsilon))
                    return true;
                
                if(symbol.equals(m_token.value)){
                    m_token = m_tokenizer.getNextToken();
                    m_token.value = m_token.value.trim();
                    _replaceToken();
                }
                else
                    return false;
            }
            else{
                if(!_ParseNonTerminal(symbol))
                    return false;
            }
        }
        return true;
    }
    
    private boolean _ParseNonTerminal(String nonterminal)
    {
        Rule rule = _NextRule(nonterminal);
        if(rule.equals(new Rule())) return false;
        if(!_ParseRightpart(rule))
            return false;
        
        return true;
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
        m_table = new HashMap<Rule,HashSet<String>>();
        HashSet<Rule> cur_rules = m_grammar.GetRules();
        for(Rule rule : cur_rules){
            HashSet<String> set;
            if(rule.IsEpsilonRule()){
                set = new HashSet<String>();
                //set.add("");
            } 
            else set = m_grammar.RuleContext(rule, m_k);
            m_table.put(rule, set);
        }
    }

    private void _FormKeyWords()
    {
        m_key_words = new ArrayList<String>();
        try
        {
            Scanner sc = new Scanner(new File("KeyWords.txt"));
            while(sc.hasNext())
            {
                m_key_words.add(sc.next());
            }
        }
        catch(Exception e)
        {
        }
    }
    
    //Shit-code here :-)
    private void _FormGrammar()  
    {
        m_grammar = new Grammar();
        //Begin terminal
        m_grammar.SetBeginTerminal("Prog");
        
        //Non terminals
        ArrayList<String> non_terminals = new ArrayList<String>();
        non_terminals.add("Prog"); non_terminals.add("Body"); non_terminals.add("Operator"); 
        non_terminals.add("Expression");
        m_grammar.SetNonTerminals(non_terminals);
        
        //Terminals
        ArrayList<String> terminals = new ArrayList<String>();
        terminals.add("int"); terminals.add("main"); terminals.add("(");
        terminals.add(")"); terminals.add("{"); terminals.add("}");
        terminals.add(";"); terminals.add(Grammar.epsilon); terminals.add("var");
        terminals.add("="); terminals.add("number");
        m_grammar.SetTerminals(terminals);
        
        //Rules
        HashSet<Rule> rules = new HashSet<Rule>();
        ArrayList<String> right_part = new ArrayList<String>();
        right_part.add("int");
        right_part.add("main");
        right_part.add("(");
        right_part.add(")");
        right_part.add("Body");
        Rule rule = new Rule("Prog", right_part);
        rules.add(rule); right_part = new ArrayList<String>();
        
        right_part.add("{"); right_part.add("Operator"); right_part.add("}");
        rule = new Rule("Body", right_part);
        rules.add(rule); right_part = new ArrayList<String>();
        
        right_part.add("Expression"); right_part.add(";"); right_part.add("Operator");
        rule = new Rule("Operator", right_part);
        rules.add(rule); right_part = new ArrayList<String>();
        
        right_part.add(Grammar.epsilon);
        rule = new Rule("Operator", right_part);
        rules.add(rule); right_part = new ArrayList<String>();
        
        right_part.add("var"); right_part.add("="); right_part.add("number");
        rule = new Rule("Expression", right_part);
        rules.add(rule);
        m_grammar.SetRules(rules);
    }
    
    private static String numer_terminal = "number";
    private static String variable_terminal = "variable";
    
    private final int m_k = 1;
    private Grammar m_grammar;
    private ArrayList<String> m_key_words;
    private HashMap<String,HashSet<Rule>> m_mapping;
    private HashMap<Rule,HashSet<String>> m_table;
    private Tokenizer m_tokenizer;
    private Tokenizer.Token m_token;
}