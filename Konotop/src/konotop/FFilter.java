package konotop;

import java.io.File;
import java.util.ArrayList;
import javax.swing.filechooser.FileFilter;

public class FFilter extends FileFilter {
    
    private ArrayList<String> acceptSuffix;
    
    public FFilter(ArrayList<String> fformat){
        acceptSuffix = fformat;
    }
    
    private String getExtension(File f){
        String fileName = f.getName();
        int pos = fileName.lastIndexOf((int)'.');
        if(pos > -1)
            return fileName.substring(pos);
        else
            return null;
    }
    
    @Override
    public boolean accept(File f){
        if(f.isDirectory()) return true;
        String suffix = getExtension(f);
        for(String s : acceptSuffix){
            if(s.toLowerCase().equals(suffix.toLowerCase()))
                return true;
        }
        return false;
    }
    
    @Override
    public String getDescription(){
        return "*.txt";
    }
}
