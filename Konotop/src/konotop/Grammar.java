package konotop;

import java.util.HashSet;
import java.util.ArrayList;

public class Grammar {
    public Grammar()
    {
        m_terminals = new ArrayList<String>();
        m_nonterminals = new ArrayList<String>();
        m_rules = new HashSet<Rule>();
    }
    
    public ArrayList<String> FindNonProductiveNonTerminals()
    {
        ArrayList<String> prev_productive_nonterminals = new ArrayList<String>();
        ArrayList<String> productive_nonterminals = GetProductiveNonTerminals(m_terminals);
        while(!productive_nonterminals.equals(prev_productive_nonterminals))
        {
            prev_productive_nonterminals = productive_nonterminals;
            productive_nonterminals = Helpers.Union(GetProductiveNonTerminals(Helpers.Union(m_terminals, prev_productive_nonterminals)), prev_productive_nonterminals);
        }
        return Helpers.Difference(m_nonterminals, productive_nonterminals);
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
    
    public  HashSet<Rule> GetRules()
    {
        return m_rules;
    }
    
    public void SetRules(HashSet<Rule> i_rules)
    {
        m_rules = i_rules;
    }
    
    private ArrayList<String> GetProductiveNonTerminals(ArrayList<String> i_productions)
    {
        ArrayList<String> res_array = new ArrayList<String>();
        for(Rule rule : m_rules)
        {
            boolean is_productive_rule = true;
            for(String str : rule.GetRightPart())
            {
                if(!i_productions.contains(str))
                {
                    is_productive_rule = false;
                    break;
                }
            }
            if(is_productive_rule)
                res_array.add(rule.GetLeftPart());
        }
        return res_array;
    }
    
    private void AddRule(Rule i_rule)
    {
        m_rules.add(i_rule);
    }
    
    private ArrayList<String> m_terminals;
    private ArrayList<String> m_nonterminals;
    private String m_begin_terminal;
    private HashSet<Rule> m_rules;
}