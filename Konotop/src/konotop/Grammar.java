package konotop;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Queue;
import java.util.Set;
import java.util.LinkedList;

public class Grammar {
    public Grammar()
    {
        m_terminals = new ArrayList<String>();
        m_nonterminals = new ArrayList<String>();
        m_rules = new HashSet<Rule>();
        minLen = Integer.MAX_VALUE;
        cyrcles = new HashSet<ArrayList<String>>();
    }
    
    public HashSet<ArrayList<Rule>> FindMinLeftRecursiveDerivations(){
        HashSet<ArrayList<Rule>> res = new HashSet<ArrayList<Rule>>();
        minLen=Integer.MAX_VALUE;
        cyrcles.clear();
        HashMap<String,HashSet<String>> g =  BuildGraph();
        for(String s : m_nonterminals){
            Bfs(g,s);
        }
        
        for(ArrayList<String> cyrc : cyrcles){
            ArrayList<Rule> cur = new ArrayList<Rule>();
            for(int i=1;i<cyrc.size();i++){
                String a = cyrc.get(i-1);
                String b = cyrc.get(i);
                for(Rule rule : m_rules){
                    if(rule.GetLeftPart().equals(a) && rule.GetRightPart().get(0).equals(b)){
                        cur.add(rule);
                        break;
                    }
                }
            }
            res.add(cur);
        }
        
        return res;
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
    
    private HashMap<String,HashSet<String>> BuildGraph(){
        HashMap<String,HashSet<String>> g = new HashMap<String,HashSet<String>>();
        for(String str : m_nonterminals){
            g.put(str,new HashSet<String>());
        }
        for(Rule cur_rule : m_rules){
            if(m_nonterminals.contains(cur_rule.GetRightPart().get(0))){
                g.get(cur_rule.GetLeftPart()).add(cur_rule.GetRightPart().get(0));
            }
        }
        return g;
    }
    private void Bfs(HashMap<String,HashSet<String>> g,String s){
        Queue<String> q = new LinkedList<String>();
        HashMap<String,Integer> used = new HashMap<String,Integer>();
        Set<String> keys = g.keySet();
        HashMap<String,String> parent = new HashMap<String,String>();
        HashMap<String,Integer> dist = new HashMap<String,Integer>();
        for(String str : keys){
            used.put(str, 0);
            parent.put(str, new String());
            dist.put(str,0);
        }
        parent.put(s,"Absent");
        q.offer(s);
        used.put(s,1);
        while(!q.isEmpty()){
            String v = q.poll();
            for(String to : g.get(v)){
                if(used.get(to).equals(0)){
                    used.put(to,1);
                    q.offer(to);
                    parent.put(to, v);
                    dist.put(to, dist.get(v)+1);
                }
                else if(to.equals(s)){
                    int cyrcle_len = dist.get(v) +1;
                    if(cyrcle_len > minLen) return;
                    else{
                        if(cyrcle_len<minLen){
                            cyrcles.clear();
                            minLen = cyrcle_len;
                        }
                        //build cyrcle, add to set
                        ArrayList<String> path = new ArrayList<String>();
                        path.add(to);
                        for(String i = v; !i.equals("Absent"); i = parent.get(i)){
                            path.add(i);
                        }
                        Collections.reverse(path);
                        cyrcles.add(path);
                    }
                }
            }
        }
        
    }
    
    private HashSet<ArrayList<String>> cyrcles;
    private int minLen;
   
    private ArrayList<String> m_terminals;
    private ArrayList<String> m_nonterminals;
    private String m_begin_terminal;
    private HashSet<Rule> m_rules;
}
