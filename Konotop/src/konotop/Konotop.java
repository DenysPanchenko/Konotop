package konotop;

import java.awt.List;
import javax.swing.JMenu;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import javax.swing.DefaultListModel;
import javax.swing.JFormattedTextField;

import javax.swing.JButton;
import javax.swing.GroupLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;

import java.util.*;

public class Konotop extends JFrame implements ActionListener, ComponentListener, ItemListener{
    
    private List                gramList;
    private List                resultList;
    private JButton             addButton;
    private JButton             delButton;
    private JMenuBar            mainMenu;
    private GroupLayout         layout;
    private JFileChooser        fileChooser;
    private DefaultListModel    gramListModel;
    private JFormattedTextField input;
    
    static Grammar gram;
    
    public Konotop(){
        
        mainMenu = new JMenuBar(); //create main menu
        JMenu menu = new JMenu("File");
        JMenuItem item = new JMenuItem("Open");
        item.setActionCommand("open");
        item.addActionListener(this);
        menu.add(item);
        item = new JMenuItem("Save");
        item.setActionCommand("save");
        item.addActionListener(this);
        //menu.add(item);
        item = new JMenuItem("Exit");
        item.setActionCommand("exit");
        item.addActionListener(this);
        menu.add(item);
        
        mainMenu.add(menu);
        
        menu = new JMenu("Algorithm");
        item = new JMenuItem("Remove useless terminals");
        item.setActionCommand("useless");
        item.addActionListener(this);
        menu.add(item);
        item = new JMenuItem("Derivation");
        item.setActionCommand("derivation");
        item.addActionListener(this);
        menu.add(item);
        
        mainMenu.add(menu);
        
        gramListModel = new DefaultListModel(); //create list model with description of grammatic
        gramListModel.addElement("The grammatics is:        ");
        gramListModel.addElement(" ");
        gramListModel.addElement(" ");
        gramListModel.addElement(" ");
        
        gramList = new List();//(gramListModel); //create list for grammatic visualisation
        gramList.setMultipleSelections(false);
        gramList.add("Grammatic is:");
        gramList.addItemListener(this);
        
        input = new JFormattedTextField(); //field for grammar rules input
        
        resultList = new List(); //text area for algorithm result visualisation
        //resultTextArea.setEditable(false);
        //resultTextArea.setText("The result:");
        //resultTextArea.setLineWrap(true);
        
        addButton = new JButton("Add");
        addButton.addActionListener(this);
        addButton.setEnabled(false);
        delButton = new JButton("Delete");
        delButton.addActionListener(this);
        delButton.setEnabled(false);
        
        setTitle("Konotop");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        layout = new GroupLayout(getContentPane());
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(gramList)
                    .addComponent(input)
                ) 
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addComponent(delButton)
                    )
                    .addComponent(resultList)
                )
        );
        
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(gramList)
                    .addComponent(resultList)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(input)
                    .addComponent(addButton)
                    .addComponent(delButton)
                )
        );
        
        setJMenuBar(mainMenu);
        pack();
        setVisible(true);
        this.addComponentListener(this);
        
        fileChooser = new JFileChooser();
        ArrayList<String> suffixes = new ArrayList<String>();
        suffixes.add(".txt");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FFilter(suffixes));
        fileChooser.addActionListener(this);
        
        gram = new Grammar();
    }
    
    public void printGrammar(List list, Grammar gram, String title){
        list.clear();
        list.add(title);
        list.add("Axiom:");
        list.add(gram.GetBeginTerminal());
        list.add("Terminals:");
        for(String s : gram.GetTerminals()){
            list.add(s);
        }
        list.add("Non-terminals");
        for(String s : gram.GetNonTerminals()){
            list.add(s);
        }
        list.add("Rules:");
        for(Rule r : gram.GetRules()){
            list.add(r.toString());
        }
    }
    
    @Override
    public void itemStateChanged(ItemEvent e){
        int pos = 0,i;
        //System.out.println(gramList.getRows());
        for(i = 0; i < gramList.getItemCount(); i++)
            if(gramList.getItem(i).equals("Rules:")){
                pos = i;
                break;
            }
        //System.out.println(new Integer(pos).toString());
        /*
        if(gramList.getSelectedIndex() > pos)
            delButton.setEnabled(true);
        else
            delButton.setEnabled(false);
         * 
         */
    }
    
    @Override
    public void componentResized(ComponentEvent e){
        if(e.getComponent().getClass().getName().equals(this.getClass().getName())){
            
        }
    }
    
    private void parseFile(File fileName){
        try{
            FileInputStream fstream = new FileInputStream(fileName.getAbsolutePath());
            DataInputStream dstream = new DataInputStream(fstream);
            BufferedReader in = new BufferedReader(new InputStreamReader(dstream));
            
            int i;
            String str,term,nonTerm;
            
            ArrayList<String>  terminals = new ArrayList<String>();
            ArrayList<String> nTerminals = new ArrayList<String>();
            
            HashSet<Rule> rules = new HashSet<Rule>();
            
            ArrayList<Integer> states = new ArrayList<Integer>();
            states.add(0);
            states.add(1);
            states.add(2);
            states.add(3);
            Integer curState = states.get(0);
            
            ArrayList<StringBuilder> fileCont = new ArrayList<StringBuilder>();
            while((str = in.readLine()) != null){
                if(str.length() > -1){
                    if((str.charAt(0) == ' ' || str.charAt(0) == '\t') || str.charAt(0) == '!'){
                        if(fileCont.size() > 0){
                            fileCont.get(fileCont.size() - 1).append(str);
                            continue;
                        }
                        else
                            throw new Exception("Parse error! Wrong begining of file");
                    }
                    fileCont.add(new StringBuilder(str));
                }
            }
            
            for(StringBuilder sb : fileCont){
                for(i = 0; i < sb.toString().length(); i++)
                    if(Character.isSpaceChar(sb.charAt(i)))
                        sb.deleteCharAt(i);
                sb.append('\n');
            }
            
            String leftPart = new String();
            ArrayList<String> rightPart = new ArrayList<String>();
            
            /*
            for(StringBuilder sb : fileCont){
                gramList.add(sb.toString());
            }
             * 
             */
            
            for(StringBuilder sb : fileCont){
                str = sb.toString();
                for(i = 0; i < str.length(); i++){
                    switch (curState){
                        case 0: 
                            if(str.charAt(i) == '#'){
                                nonTerm = Helpers.getNonTerminal(str, i + 1).getFirst();   
                                i = Helpers.getNonTerminal(str, i + 1).getSecond();
                                nTerminals.add(nonTerm);
                                leftPart = nonTerm;
                                gram.SetBeginTerminal(nonTerm);
                                curState = 1;
                            }
                            else
                                throw new Exception("Parsing error! No axiom as first string");
                            break;
                        case 1:
                            if(str.length() > i + 2){
                                if((str.charAt(i) == ':' && str.charAt(i + 1) == ':') && (str.charAt(i + 2) == '=')){
                                    i += 2;
                                    curState = 2;
                                    break;
                                }
                                else
                                    throw new Exception("Parsing error! Wrong end of rule");
                            }
                            break;
                        case 2:
                            if(str.charAt(i) == '#'){
                                nonTerm = Helpers.getNonTerminal(str, i + 1).getFirst();   
                                i = Helpers.getNonTerminal(str, i + 1).getSecond();
                                //gramList.add(str.substring(i));
                                if(!nTerminals.contains(nonTerm)) 
                                    nTerminals.add(nonTerm);
                                rightPart.add(nonTerm);
                            }
                            else
                            if(str.charAt(i) == '!'){
                                Rule r = new Rule(leftPart, new ArrayList<String>(rightPart));
                                rules.add(r);
                                rightPart.clear();
                            }
                            else
                            if(str.charAt(i) == '\n'){
                                Rule r = new Rule(leftPart,new ArrayList<String>(rightPart));
                                rules.add(r);
                                rightPart.clear();
                                curState = 3;
                            }
                            else{
                                term = Helpers.getTerminal(str, i).getFirst();
                                i = Helpers.getTerminal(str, i).getSecond();
                                terminals.add(term);
                                rightPart.add(term);
                            }
                            break;
                        case 3:
                            if(str.charAt(i) == '#'){
                                nonTerm = Helpers.getNonTerminal(str, i + 1).getFirst();   
                                i = Helpers.getNonTerminal(str, i + 1).getSecond();
                                if(!nTerminals.contains(nonTerm)) 
                                    nTerminals.add(nonTerm);
                                leftPart = nonTerm;
                                curState = 1;
                            }
                            else
                                throw new Exception("Parsing error! No axiom as first string");
                            break;
                    }
                }
            }
            gram.SetNonTerminals(nTerminals);
            gram.SetTerminals(terminals);
            gram.SetRules(rules);
            printGrammar(gramList,gram,"Grammar is:");
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }
    
    @Override
    public void componentHidden(ComponentEvent e){
        
    }
    
    @Override
    public void componentShown(ComponentEvent e){
        
    }
    
    @Override
    public void componentMoved(ComponentEvent e){
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
       if("exit".equals(e.getActionCommand()))
           dispose();
       if("open".equals(e.getActionCommand())){
           int returnVal = fileChooser.showOpenDialog(this);
           if(returnVal == JFileChooser.APPROVE_OPTION){
               File bnfFile = fileChooser.getSelectedFile();
               parseFile(bnfFile);
           }
       }
       if("useless".equals(e.getActionCommand())){
           resultList.clear();
           resultList.add("Useless non-terminals:");
           for(String s : gram.FindNonProductiveNonTerminals()){
               resultList.add(s);
           }
           if(gram.FindNonProductiveNonTerminals().isEmpty())
               resultList.add("There are no non-productive non-terminals");
       }
       if("derivation".equals(e.getActionCommand())){
           resultList.clear();
           resultList.add("Minimum derivations is:");
           for(ArrayList<Rule> al : gram.FindMinLeftRecursiveDerivations()){
               for(Rule r : al){
                   resultList.add(r.toString());
               }
           }
       }
       if("delete".equals(e.getActionCommand())){
           
       }
       if("add".equals(e.getActionCommand())){
           
       }
    }
    
    public static void main(String[] args) {
        Konotop app = new Konotop();
        // TODO code application logic here
        //Grammar gram = new Grammar();
        /*
        gram.SetBeginTerminal("A");
        ArrayList<String> N = new ArrayList<String>();
        Collections.addAll(N, "A","B","C","P","K","M");
        gram.SetNonTerminals(N);
        ArrayList<String> T = new ArrayList<String>();
        Collections.addAll(T, "a","b","c");
        
        ArrayList<String> k1 = new ArrayList<String>();
        Collections.addAll(k1, "B","C");
        ArrayList<String> k2 = new ArrayList<String>();
        Collections.addAll(k2, "B","P");
        ArrayList<String> k3 = new ArrayList<String>();
        Collections.addAll(k3, "A","K");
        ArrayList<String> k4 = new ArrayList<String>();
        Collections.addAll(k4, "M");
        
        Rule r1 = new Rule("A", k1);
        Rule r2 = new Rule("A", k2);
        Rule r3 = new Rule("B", k3);
        Rule r4 = new Rule("B", k4);
        HashSet<Rule> rules = new HashSet<Rule>();
        rules.add(r1);
        rules.add(r2);
        rules.add(r3);
        rules.add(r4);
        
        gram.SetRules(rules);
        HashSet<ArrayList<Rule>> res =  gram.FindMinLeftRecursiveDerivations();
         * 
         */
    }
};