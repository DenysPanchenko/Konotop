package konotop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Helpers {
    static boolean tablesAreEquals(HashMap<String,HashSet<ArrayList<String>>> prev,HashMap<String,HashSet<ArrayList<String>>> cur ){
            Set<String> keySet = prev.keySet();
            for(String leftPart : keySet){
                HashSet<ArrayList<String>> prevRightPart = prev.get(leftPart);
                HashSet<ArrayList<String>> curRightPart = cur.get(leftPart);
                if(!prevRightPart.containsAll(curRightPart) || !curRightPart.containsAll(prevRightPart)) return false;
                
            }
            return true;
    }
    
    static HashSet<ArrayList<String>> plusK(HashSet<ArrayList<String>> left, HashSet<ArrayList<String>> right, int k){
        HashSet<ArrayList<String>> res = new HashSet<ArrayList<String>>();
        for(ArrayList<String> l : left){
            for(ArrayList<String> r : right){
                ArrayList<String> union = new ArrayList<String>();
                union.addAll(l);
                union.addAll(r);
                ArrayList<String> unionk = new ArrayList<String>();
                /*
                for(int i=0;(i<k)&&(i<union.size());i++){ 
                    unionk.add(union.get(i));
                }
                res.add(unionk);
                */
                int j=0;
                for(int i=0;i<union.size();i++){
                    if(j>=k) break;
                    if(union.get(i).equals("$") && (i+1)<union.size()) continue;
                    unionk.add(union.get(i));
                    j++;
                }
                res.add(unionk);
                
            }
        }
        return res;
    }
    
    static ArrayList<String> Union(ArrayList<String> i_set1, ArrayList<String> i_set2)
    {
        ArrayList<String> union = new ArrayList<String>();
        union.addAll(i_set1);
        for(String str : i_set2)
        {
            if(!i_set1.contains(str))
                union.add(str);
        }
        return union;
    }
    
    static ArrayList<String> Difference(ArrayList<String> i_set1, ArrayList<String> i_set2)
    {
        ArrayList<String> difference = new ArrayList<String>();
        for(String str : i_set1)
        {
            if(!i_set2.contains(str))
            {
                difference.add(str);
            }
        }
        return difference;
    }
}
