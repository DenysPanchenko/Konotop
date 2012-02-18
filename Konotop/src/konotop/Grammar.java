package konotop;

import java.util.HashSet;
import java.util.ArrayList;

public class Grammar {
    public Grammar()
    {
        m_terminals = new ArrayList<String>();
        m_nonterminals = new ArrayList<String>();
        m_rules = new HashSet<Pair<String, String>>();
    }
    
    public ArrayList<String> GetTerminals()
    {
        return m_terminals;
    }
    
    public void SetTerminals(ArrayList<String> i_terminals)
    {
        m_terminals = i_terminals;
    }
    
    public ArrayList<String> GetNonTerminals()
    {
        return m_nonterminals;
    }
    
    public void SetNonTerminals(ArrayList<String> i_nonterminals)
    {
        m_nonterminals = i_nonterminals;
    }
    
    public String GetBeginTerminal()
    {
        return m_begin_terminal;
    }
    
    public void SetBeginTerminal(String i_begin_terminal)
    {
        m_begin_terminal = i_begin_terminal;
    }
    
    public  HashSet<Pair<String, String>> GetRules()
    {
        return m_rules;
    }
    
    public void SetRules(HashSet<Pair<String, String>> i_rules)
    {
        m_rules = i_rules;
    }
    
    private ArrayList<String> m_terminals;
    private ArrayList<String> m_nonterminals;
    private String m_begin_terminal;
    private HashSet<Pair<String, String>> m_rules;
}
