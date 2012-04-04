package konotop;

import java.util.ArrayList;

public class Rule {
    
    ArrayList<String> rightTerminals;
    ArrayList<String> rightNterminals;
    
    public Rule()
    {
        rightTerminals = new ArrayList<String>();
        rightNterminals = new ArrayList<String>();
        m_rule = new Pair(new String(), new ArrayList<String>());
    }
    
    public Rule(String i_left, ArrayList<String> i_right)
    {
        m_rule = new Pair(i_left, i_right);
    }
    
    public Pair<String, ArrayList<String>> GetRule()
    {
        return m_rule;
    }
    
    public void SetRule(Pair<String, ArrayList<String>> i_rule)
    {
        m_rule = i_rule;
    }
    
    public void AddSymbolToRightPart(String i_symbol)
    {
        ArrayList<String> right_part = m_rule.getSecond();
        right_part.add(i_symbol);
        m_rule.setSecond(right_part);
    }
    
    public String GetLeftPart()
    {
        return m_rule.getFirst();
    }
    
    public void SetLeftPart(String i_left_part)
    {
        m_rule.setFirst(i_left_part);
    }
    
    public ArrayList<String> GetRightPart()
    {
        return m_rule.getSecond();
    }
    
    public void SetRightPart(ArrayList<String> i_left_part)
    {
        m_rule.setSecond(i_left_part);
    }
    
    private Pair<String, ArrayList<String>> m_rule;
    
    public ArrayList<String> GetRightNonTerminals(){
        return rightNterminals;
    }
    
    public void SetRightNonTerminals(ArrayList<String> nTerm){
        rightNterminals = new ArrayList<String>(nTerm);
    }
    
    public ArrayList<String> GetRightTerminals(){
        return rightTerminals;
    }
    
    public void SetRightTerminals(ArrayList<String> term){
        rightTerminals = new ArrayList<String>(term);
    }
    
    @Override
    public String toString(){
        String left_part = m_rule.getFirst();
        StringBuilder right_part = new StringBuilder();
        for(String s : m_rule.getSecond())
            right_part.append(s);
        if(m_rule.getSecond().isEmpty())
            System.out.println("Empty");
        return new String(left_part + "->" + right_part.toString());
    }
    
    @Override
    public boolean equals(Object i_obj)
    {
        if(i_obj == this)
            return true;
        
        if(! (i_obj instanceof Rule))
            return false;
        
        Rule rule = (Rule)i_obj;
        
        return m_rule.equals(rule.m_rule);
    }
}
