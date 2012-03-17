package konotop;

import java.util.ArrayList;

public class Helpers {
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
