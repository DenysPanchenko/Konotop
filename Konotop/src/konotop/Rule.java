package konotop;

import java.util.ArrayList;

public class Rule {
    public Rule()
    {
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
}
