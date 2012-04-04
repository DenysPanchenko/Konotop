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
        m_lastFirstK=-1;
        m_lastFollowK=-1;
    }
    
    public HashSet<ArrayList<String>> FirstChain(int k, ArrayList<String> a){
        HashSet<ArrayList<String>> res = First(k, a.get(0));
        
        for(int i=1;i<a.size();i++){
            res = Helpers.plusK(res, First(k,a.get(i)), k);
        }
        
        return res;
    }
    
    //K == 1!!!!!!!!!!!
    public HashSet<String> RuleContext(Rule rule){
        HashSet<ArrayList<String>> res = FirstChain(1, rule.GetRightPart());
        res = Helpers.plusK(res,Follow(1, rule.GetLeftPart()), 1);
        HashSet<String> unique_res = new HashSet<String>();
        for(ArrayList<String> chain : res)
        {
            unique_res.add(chain.get(0));
        }
        return unique_res;
    }
    
    public HashSet<ArrayList<String>> First(int k, String a){
       
        if(m_lastFirstK==k && m_allFirstK!=null){
            return m_allFirstK.get(a);
        }
        
        if(m_terminals.contains(a)){
             HashSet<ArrayList<String>> res = new HashSet<ArrayList<String>>();
            ArrayList<String> resList = new ArrayList<String>();
            resList.add(a);
            res.add(resList);
            return res;
        }
        
        HashMap<String,HashSet<ArrayList<String>>> table = new HashMap<String,HashSet<ArrayList<String>>>();
        HashMap<String,HashSet<ArrayList<String>>> prevTable = new HashMap<String,HashSet<ArrayList<String>>>();
        
        for(String nt : m_nonterminals){
            table.put(nt, new HashSet<ArrayList<String>>());
            prevTable.put(nt, new HashSet<ArrayList<String>>());
        }
        for(String t : m_terminals){
            HashSet<ArrayList<String>> set = new HashSet<ArrayList<String>>();
            ArrayList<String> list = new ArrayList<String>();
            list.add(t);
            set.add(list);
            table.put(t, set);
            prevTable.put(t, new HashSet<ArrayList<String>>());
        }
        
        while(!Helpers.tablesAreEquals(table, prevTable)){
            prevTable = copyTable(table);
            
            for(Rule rule : m_rules){
                String leftPart = rule.GetLeftPart();
                ArrayList<String> rightPart = rule.GetRightPart();
                HashSet<ArrayList<String>> curFirst = new HashSet<ArrayList<String>>();
                curFirst = prevTable.get(rightPart.get(0));
                for(int i=1;i<rightPart.size();i++){
                    curFirst = Helpers.plusK(curFirst, prevTable.get(rightPart.get(i)), k);
                }
                HashSet<ArrayList<String>> m = table.get(leftPart);
                m.addAll(curFirst);
                table.put(leftPart, m);
            }
        }
        m_allFirstK = table;
        return table.get(a);
    }
    
    public HashSet<ArrayList<String>> Follow(int k,String a){
        //HashSet<ArrayList<String>> res = new HashSet<ArrayList<String>>();
        
        if(m_lastFollowK==k && m_allFollowK!=null){
            return m_allFollowK.get(a);
        }
        
        if(m_lastFirstK!=k || m_allFirstK==null){
            First(k, a);
        }
        
        HashMap<String,HashSet<ArrayList<String>>> table = new HashMap<String,HashSet<ArrayList<String>>>();
        HashMap<String,HashSet<ArrayList<String>>> prevTable = new HashMap<String,HashSet<ArrayList<String>>>();
        
        for(String nt : m_nonterminals){
            table.put(nt, new HashSet<ArrayList<String>>());
            prevTable.put(nt, new HashSet<ArrayList<String>>());
        }
        
        // begin ???
        
        HashSet<ArrayList<String>> beginTerminalSet = table.get(m_begin_terminal);
        ArrayList<String> beginTerminalList = new ArrayList<String>();
        beginTerminalList.add("$"); // epsilon
        beginTerminalSet.add(beginTerminalList);
        table.put(m_begin_terminal, beginTerminalSet);
        
        for(Rule rule : m_rules){
            String leftPart = rule.GetLeftPart();
            ArrayList<String> rightPart = rule.GetRightPart();
            if(leftPart.equals(m_begin_terminal)){
                for(int i=rightPart.size()-1;i>=0;i--){
                        String cur_symbol = rightPart.get(i);
                        if(m_nonterminals.contains(cur_symbol)){
                            HashSet<ArrayList<String>> curFollow = new HashSet<ArrayList<String>>();
                            if((i+1)<rightPart.size()){
                                curFollow = m_allFirstK.get(rightPart.get(i+1));
                            }
                            else{
                                ArrayList<String> epsList = new ArrayList<String>();
                                epsList.add("$");
                                curFollow.add(epsList);
                            }
                            for(int j=i+2;j<rightPart.size();j++){
                                curFollow = Helpers.plusK(curFollow, m_allFirstK.get(rightPart.get(j)), k);
                            }
                            HashSet<ArrayList<String>> m = table.get(cur_symbol);
                            m.addAll(curFollow);
                            table.put(cur_symbol,curFollow);
                        }
                    }    
            }
        }
        
        //end ??? 
        
        while(!Helpers.tablesAreEquals(prevTable, table)){
            prevTable = copyTable(table);
            for(Rule rule : m_rules){
                String leftPart = rule.GetLeftPart();
                ArrayList<String> rightPart = rule.GetRightPart();
                for(int i=rightPart.size()-1;i>=0;i--){
                    String cur_symbol = rightPart.get(i);
                    if(m_nonterminals.contains(cur_symbol)){
                        HashSet<ArrayList<String>> curFollow = new HashSet<ArrayList<String>>();
                        if((i+1)<rightPart.size()){
                            curFollow = m_allFirstK.get(rightPart.get(i+1));
                        }
                        else{
                            ArrayList<String> epsList = new ArrayList<String>();
                            epsList.add("$");
                            curFollow.add(epsList);
                        }
                        for(int j=i+2;j<rightPart.size();j++){
                            curFollow = Helpers.plusK(curFollow, m_allFirstK.get(rightPart.get(j)), k);
                        }
                        curFollow = Helpers.plusK(curFollow, prevTable.get(leftPart), k);
                        HashSet<ArrayList<String>> m = table.get(cur_symbol);
                        m.addAll(curFollow);
                        table.put(cur_symbol,m);
                    }
                }
            }
        }
        m_allFollowK=table;
        return table.get(a);
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
    
    public boolean IsTerminal(String i_symbol)
    {
        return m_terminals.contains(i_symbol);
    }
    
    public boolean IsNonTerminal(String i_symbol)
    {
        return m_nonterminals.contains(i_symbol);
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
    
    private HashMap<String,HashSet<ArrayList<String>>> copyTable(HashMap<String,HashSet<ArrayList<String>>> m){
        HashMap<String,HashSet<ArrayList<String>>> res = new HashMap<String,HashSet<ArrayList<String>>>();
        Set<String> keySet = m.keySet();
        for(String key:keySet){
            String new_key = new String(key);
            HashSet<ArrayList<String>> set = m.get(key);
            HashSet<ArrayList<String>> new_set = new HashSet<ArrayList<String>>();
            for(ArrayList<String> array : set){
                 ArrayList<String> new_array = new ArrayList<String>();
                 for(String string : array){
                     String new_string = new String(string);
                     new_array.add(new_string);
                 }
                 new_set.add(new_array);
            }
            res.put(new_key, new_set);
        }
        return res;
    }
    
    private HashSet<ArrayList<String>> cyrcles;
    private int minLen;
   
    private HashMap<String,HashSet<ArrayList<String>>> m_allFirstK;
    private int m_lastFirstK;
    private HashMap<String,HashSet<ArrayList<String>>> m_allFollowK;
    private int m_lastFollowK;
    
    private ArrayList<String> m_terminals;
    private ArrayList<String> m_nonterminals;
    private String m_begin_terminal;
    private HashSet<Rule> m_rules;
}
