package parser;

import core.Grammar;
import core.Rule;
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
        m_wrong_lexem = new String();
        m_possible_lexems = new HashSet<ArrayList<String>>();
    }
    //todo: exclude forming grammar
    
    private void _replaceToken(){
        if(m_token.type == Tokenizer.TokType.NUMBER)
            m_token.value = numer_terminal;
        else if(m_token.type == Tokenizer.TokType.KWORIDENT 
                && !(m_key_words.contains(m_token.value)))
            m_token.value = variable_terminal;
        //else if(m_token.type == Tokenizer.TokType.DIRECTIVE)
          //  m_token.value = directive_terminal;
    }
    
    public boolean Parse()
    {
        m_token = m_tokenizer.getNextToken();
        m_num_of_token = 1;
        _replaceToken();
        String begin_non_terminal = m_grammar.GetBeginNonTerminal();
        if(_NextRule(begin_non_terminal).IsEpsilonRule())
            return false;
        boolean result = _ParseNonTerminal(begin_non_terminal);
        if(!result) m_wrong_lexem = m_token.value;
        return result;
    }
    
    public int GetNumOfToken()
    {
        return m_num_of_token;
    }
    
    public String GetWrongLexem()
    {
        return m_wrong_lexem;
    }
    
    public HashSet<ArrayList<String>> GetPossibleLexems()
    {
        return m_possible_lexems;
    }
    
    public void SetGrammar(Grammar i_grammar)
    {
        m_grammar = i_grammar;
        _CreateMapping();
        _FormTable();
    }
           
    public void SetProgramText(String i_program_text)
    {
        m_tokenizer.setText(i_program_text);
    }
    
    private Rule _NextRule(String nonterm){
        HashSet<Rule> prog_rules = m_mapping.get(nonterm);
        Rule rule = new Rule();
        m_possible_lexems = new HashSet<ArrayList<String>>();
        for(Rule cur_rule : prog_rules)
        {
            ArrayList<String> terminals = new ArrayList<String>();
            HashSet<String> cur_set = m_table.get(cur_rule);
            if(cur_set.isEmpty() == false)
            {
            for(String terminal : cur_set)
            {
                terminals.add(terminal);
            }
            m_possible_lexems.add(terminals);
            }
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
                    m_num_of_token++;
                    while(m_token.type.equals(Tokenizer.TokType.SPACE))
                    {    m_token = m_tokenizer.getNextToken();
                        m_num_of_token++;
                    }
                    m_token.value = m_token.value.trim();
                    _replaceToken();
                    m_possible_lexems.clear();
                }
                else
                {
                    ArrayList<String> terminals = new ArrayList<String>();
                    terminals.add(symbol);
                    m_possible_lexems.add(terminals);
                    return false;
                }
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
    
    
    
    private final static String numer_terminal = "number";
    private final static String variable_terminal = "variable";
    private final static String directive_terminal = "directive";
    
    private final int m_k = 1;
    private Grammar m_grammar;
    private ArrayList<String> m_key_words;
    private HashMap<String,HashSet<Rule>> m_mapping;
    private HashMap<Rule,HashSet<String>> m_table;
    private Tokenizer m_tokenizer;
    private Tokenizer.Token m_token;
    //For error notification
    private int m_num_of_token;
    private String m_wrong_lexem;
    private HashSet<ArrayList<String>> m_possible_lexems;
}