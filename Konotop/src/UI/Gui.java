package UI;

import parser.Tokenizer;

import java.awt.Color;

import core.Grammar;
import core.Helpers;
import core.Rule;

import java.awt.List;
import javax.swing.JMenu;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.JFormattedTextField;
import javax.swing.event.ChangeListener;

import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.GroupLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.io.File;

import javax.swing.Timer;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.JOptionPane;
import parser.CParser;
import parser.FAParser;


public class Gui extends JFrame implements ActionListener, ComponentListener, ItemListener, ChangeListener {
    private JMenuBar            mainMenu;
    private JFileChooser        fileChooser;
    private JTabbedPane         tabbedPane;
    
    private GroupLayout         layoutP1;
    private JPanel              panel1;
    private List                gramListP1;
    private List                resultListP1;
    private JButton             addButtonP1;
    private JButton             delButtonP1;
    private JFormattedTextField inputP1;
    
    private GroupLayout   layoutP2;
    private JPanel        panel2;
    private List          gramListP2;
    private ColorTextPane progTextPaneP2;
    private JTextField    textFieldP2;
    private JScrollPane   scrollPaneP2;
    private JButton       runParseP2;
   
    private Grammar gramP1;
    private Grammar gramP2;
    private FAParser FAparser;
    private CParser cParser;
    private String program;
    private Timer timer;
    
    private int errorPos;
    private ArrayList<String> keywords;

    public Gui() {
        
        mainMenu = new JMenuBar(); //create main menu
        JMenu menu = new JMenu("File");
        JMenuItem item = new JMenuItem("Import gram from file");
        item.setActionCommand("import_gram");
        item.addActionListener(this);
        menu.add(item);
        
        item = new JMenuItem("Import prog from file");
        item.setActionCommand("import_prog");
        item.addActionListener(this);
        item.setEnabled(false);
        menu.add(item);
        
        menu.addSeparator();
        
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
        
        menu.addSeparator();
        
        item = new JMenuItem("FirstK");
        item.setActionCommand("firstk");
        item.addActionListener(this);
        menu.add(item);
        
        item = new JMenuItem("FollowK");
        item.setActionCommand("followk");
        item.addActionListener(this);
        menu.add(item);

        mainMenu.add(menu);
        
        gramListP1 = new List();//(gramListModel); //create list for grammatic visualisation
        gramListP1.setMultipleMode(false);
        gramListP1.add("Grammar is:");
        gramListP1.addItemListener(this);

        inputP1 = new JFormattedTextField(); //field for grammar rules input

        resultListP1 = new List(); //text area for algorithm result visualisation

        addButtonP1 = new JButton("Add");
        addButtonP1.setActionCommand("add");
        addButtonP1.addActionListener(this);
        addButtonP1.setEnabled(true);
        delButtonP1 = new JButton("Delete");
        delButtonP1.setActionCommand("del");
        delButtonP1.addActionListener(this);
        delButtonP1.setEnabled(false);

        setTitle("Konotop");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        panel1 = new JPanel();
        
        layoutP1 = new GroupLayout(panel1);
        panel1.setLayout(layoutP1);
        layoutP1.setAutoCreateGaps(true);
        layoutP1.setAutoCreateContainerGaps(true);

        layoutP1.setHorizontalGroup(
                layoutP1.createSequentialGroup()
                .addGroup(layoutP1.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(gramListP1)
                    .addComponent(inputP1)
                )
                .addGroup(layoutP1.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layoutP1.createSequentialGroup()
                        .addComponent(addButtonP1)
                        .addComponent(delButtonP1)
                    )
                    .addComponent(resultListP1)
                )
        );

        layoutP1.setVerticalGroup(
                layoutP1.createSequentialGroup()
                .addGroup(layoutP1.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(gramListP1)
                    .addComponent(resultListP1)
                )
                .addGroup(layoutP1.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(inputP1)
                    .addComponent(addButtonP1)
                    .addComponent(delButtonP1)
                )
        );
        
        gramListP2 = new List();
        textFieldP2 = new JTextField();
        textFieldP2.setEditable(false);
        
        progTextPaneP2 = new ColorTextPane();
        //progTextPaneP2.setWrapStyleWord(true);
        scrollPaneP2 = new JScrollPane(progTextPaneP2);
        
        runParseP2 = new JButton("Parse");
        runParseP2.setActionCommand("parse");
        runParseP2.addActionListener(this);
        
        panel2 = new JPanel();
        
        layoutP2 = new GroupLayout(panel2);
        panel2.setLayout(layoutP2);
        layoutP2.setAutoCreateGaps(true);
        layoutP2.setAutoCreateContainerGaps(true);
        
        layoutP2.setVerticalGroup(
                        layoutP2.createSequentialGroup()
                        .addGroup(layoutP2.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(textFieldP2)
                            .addComponent(runParseP2)
                        )
                        .addGroup(layoutP2.createParallelGroup(GroupLayout.Alignment.LEADING) 
                            .addComponent(gramListP2)
                            .addComponent(scrollPaneP2)
                        )
                    
                );
        layoutP2.setHorizontalGroup(
                    layoutP2.createSequentialGroup()
                        .addGroup(layoutP2.createParallelGroup(GroupLayout.Alignment.LEADING) 
                            .addComponent(textFieldP2)
                            .addComponent(gramListP2)
                        ) 
                        .addGroup(layoutP2.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(runParseP2) 
                            .addComponent(scrollPaneP2)
                        )
                );
        
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("First task", panel1);
        tabbedPane.addTab("Second task", panel2);
        tabbedPane.addChangeListener(this);
        add(tabbedPane);
        
        setJMenuBar(mainMenu);
        pack();
        setVisible(true);
        this.addComponentListener(this);
        
        errorPos = -1;
        timer = new Timer(750,this);
        timer.setActionCommand("time_out");
        timer.start();
        
        fileChooser = new JFileChooser();
        ArrayList<String> suffixes = new ArrayList<String>();
        suffixes.add(".txt");
        fileChooser.setAcceptAllFileFilterUsed(false);
//        fileChooser.setFileFilter(new FFilter(suffixes));
        fileChooser.addActionListener(this);

        gramP1 = new Grammar();
        gramP2 = new Grammar();
        
        program = new String("");
        cParser = new CParser(program,gramP2);
        
        FAparser = new FAParser();
    }
    
    @Override
    public void stateChanged(ChangeEvent e){
        JTabbedPane tp = (JTabbedPane)e.getSource();
        if(tp.getSelectedIndex() == 0){
            mainMenu.getMenu(0).getItem(1).setEnabled(false);
            mainMenu.getMenu(1).getItem(0).setEnabled(true);
            mainMenu.getMenu(1).getItem(1).setEnabled(true);
            mainMenu.getMenu(1).getItem(3).setEnabled(true);
            mainMenu.getMenu(1).getItem(4).setEnabled(true);
        }
        if(tp.getSelectedIndex() == 1){
            mainMenu.getMenu(0).getItem(1).setEnabled(true);
            mainMenu.getMenu(1).getItem(0).setEnabled(false);
            mainMenu.getMenu(1).getItem(1).setEnabled(false);
            mainMenu.getMenu(1).getItem(3).setEnabled(false);
            mainMenu.getMenu(1).getItem(4).setEnabled(false);
        }
    }
    
    private void printGrammar(List list, Grammar gram, String title){
        list.clear();
        list.add(title);
        list.add("Axiom:");
        list.add(gram.GetBeginNonTerminal());
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
        for(i = 0; i < gramListP1.getItemCount(); i++)
            if(gramListP1.getItem(i).equals("Rules:")){
                pos = i;
                break;
            }
        //System.out.println(new Integer(pos).toString());
        
        if(gramListP1.getSelectedIndex() > pos)
            delButtonP1.setEnabled(true);
        else
            delButtonP1.setEnabled(false);
    }

    @Override
    public void componentResized(ComponentEvent e){

    }

    private void parseFile(File fileName){
        
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
       if("import_gram".equals(e.getActionCommand())){
           int returnVal = fileChooser.showOpenDialog(this);
           if(returnVal == JFileChooser.APPROVE_OPTION){
                File bnfFile = fileChooser.getSelectedFile();
                    try{
                        FAparser.clear();
                        FAparser.setInputFile(bnfFile);
                        FAparser.parseFile();
                        if(tabbedPane.getSelectedIndex() == 0){
                            gramP1 = FAparser.getGrammar();
                            printGrammar(gramListP1, gramP1, "Grammar is");
                        }
                        if(tabbedPane.getSelectedIndex() == 1){
                            gramP2 = FAparser.getGrammar();
                            printGrammar(gramListP2, gramP2, "Grammar is");
                            keywords = Helpers.FormKeyWords();
                        }
                    }
                    catch(Exception exc){
                        JOptionPane.showMessageDialog(rootPane, exc.toString());
                    }
           }
       }
       if("parse".equals(e.getActionCommand())){
           program = new String(progTextPaneP2.getText());
           cParser.SetGrammar(gramP2);
           cParser.SetProgramText(program);
           boolean result = cParser.Parse();
           if(result){
               textFieldP2.setText("Parsing successfully completed");
               errorPos = -1;
           }
           else{
               String output = "Parsing failed. Wrong lexem - " + cParser.GetWrongLexem();
               output += " Expected results:";
               for(ArrayList<String> terminals : cParser.GetPossibleLexems()){
                   output += terminals.get(0);
                   output += "; ";
               }
               output += " Number of lexem:"+cParser.GetNumOfToken();
               errorPos = cParser.GetNumOfToken();
               textFieldP2.setText(output);
           }
       }
       if("import_prog".equals(e.getActionCommand())){
           int returnVal = fileChooser.showOpenDialog(this);
           if(returnVal == JFileChooser.APPROVE_OPTION){
                File progFile = fileChooser.getSelectedFile();
                    try{
                        FileInputStream fstream = new FileInputStream(progFile);
                        DataInputStream dstream = new DataInputStream(fstream);
                        BufferedReader in = new BufferedReader(new InputStreamReader(dstream));
                        StringBuilder sb = new StringBuilder();
                        String t;
                        while((t = in.readLine()) != null){
                            sb.append(t);
                            sb.append("\n");
                        }
                        program = sb.toString();
                        Tokenizer tok = new Tokenizer(program);
                        Tokenizer.Token curToken;
                        do{
                            curToken = tok.getNextToken();
                            if(curToken.type.equals(Tokenizer.TokType.UNKNOWN))
                                progTextPaneP2.append(Color.red, curToken.value);
                            else
                            if(curToken.type.equals(Tokenizer.TokType.LITERAL))
                                progTextPaneP2.append(Color.CYAN, curToken.value);
                            else
                            if(curToken.type.equals(Tokenizer.TokType.KWORIDENT)){
                                if(keywords.indexOf(curToken.value) != -1)
                                    progTextPaneP2.append(Color.BLUE, curToken.value);
                                else
                                    progTextPaneP2.append(Color.GREEN, curToken.value);
                            }
                            else
                            if(curToken.type.equals(Tokenizer.TokType.ASSIGNOP))
                                progTextPaneP2.append(Color.ORANGE, curToken.value);
                            else
                            if(curToken.type.equals(Tokenizer.TokType.COMMENT))
                                progTextPaneP2.append(Color.gray, curToken.value);
                            else
                            if(curToken.type.equals(Tokenizer.TokType.NUMBER))
                                progTextPaneP2.append(Color.MAGENTA, curToken.value);
                            else
                            if(curToken.type.equals(Tokenizer.TokType.SPACE))
                                progTextPaneP2.append(Color.white, curToken.value);
                            else
                                progTextPaneP2.append(Color.black, curToken.value);
                        }while(!curToken.value.isEmpty());
                    }
                    catch(Exception exc){
                        JOptionPane.showMessageDialog(rootPane, exc.toString());
                    }
           }
       }
       if("useless".equals(e.getActionCommand())){
           resultListP1.clear();
           resultListP1.add("Useless non-terminals:");
           for(String s : gramP1.FindNonProductiveNonTerminals()){
               resultListP1.add(s);
           }
           if(gramP1.FindNonProductiveNonTerminals().isEmpty())
               resultListP1.add("There are no non-productive non-terminals");
       }
       if("derivation".equals(e.getActionCommand())){
           resultListP1.clear();
           resultListP1.add("Minimum derivations is:");
           for(ArrayList<Rule> al : gramP1.FindMinLeftRecursiveDerivations()){
               for(Rule r : al){
                   resultListP1.add(r.toString());
               }
           }
       }
       if("del".equals(e.getActionCommand())){
           int index = gramListP1.getSelectedIndex();
           int j, pos = 0;
           for(j = 0; j < gramListP1.getItemCount(); j++){
               if("Rules:".equals(gramListP1.getItem(j))){
                   pos = j;
                   break;
               }
           }
           index -= (pos + 1);
           j = 0;
           for(Rule r : gramP1.GetRules()){
               if(j == index){
                   HashSet<Rule> nRules = gramP1.GetRules();
                   nRules.remove(r);
                   boolean need;
                   ArrayList<String> nnTerm = new ArrayList<String>(gramP1.GetNonTerminals());
                   for(String s : r.GetRightNonTerminals()){
                       need = false;
                       for(Rule ru : gramP1.GetRules()){
                           for(String s2 : ru.GetRightNonTerminals())
                               if(s2.equals(s))
                                   need = true;
                           if(ru.GetLeftPart().equals(s))
                               need = true;
                       }
                       if(!need)
                           if(!gramP1.GetBeginNonTerminal().equals(s))                               
                               nnTerm.remove(s);
                   }
                   
                   need = false;
                       for(Rule ru : gramP1.GetRules()){
                           for(String s2 : ru.GetRightNonTerminals())
                               if(s2.equals(r.GetLeftPart()))
                                   need = true;
                           if(ru.GetLeftPart().equals(r.GetLeftPart()))
                               need = true;
                       }
                       if(!need)
                           if(!gramP1.GetBeginNonTerminal().equals(r.GetLeftPart()))                               
                               nnTerm.remove(r.GetLeftPart());
                   
                   gramP1.SetNonTerminals(nnTerm);
                   
                   ArrayList<String> nTerm = new ArrayList<String>(gramP1.GetTerminals());
                   for(String s : r.GetRightTerminals()){
                       need = false;
                       for(Rule ru : gramP1.GetRules())
                           for(String s2 : ru.GetRightTerminals())
                               if(s2.equals(s))
                                   need = true;
                       if(!need)
                           nTerm.remove(s);
                   }
                   gramP1.SetTerminals(nTerm);
                   
                   gramP1.SetRules(nRules);
                   printGrammar(gramListP1, gramP1, "Grammar is:");
                   break;
               }
               j++;
           }
       }
       if("add".equals(e.getActionCommand())){
           String str = inputP1.getText();
           if(str.isEmpty())
               JOptionPane.showMessageDialog(this, "Input string cannot be empty!");
           else{
               try{
                FAparser.parseInputString(str);
                gramP1 = FAparser.getGrammar();
                printGrammar(gramListP1, gramP1, "Grammar is:");
               }
               catch(Exception exc){
                   JOptionPane.showMessageDialog(rootPane, exc.toString());
               }
           }
       }
       
       if("firstk".equals(e.getActionCommand())){
           StringBuilder sb = new StringBuilder();
           resultListP1.clear();
           resultListP1.add("FirstK table is:");
           String result = JOptionPane.showInputDialog(rootPane, "Input k");
               try{
                   int i = Integer.parseInt(result);
                   for(String nt: gramP1.GetNonTerminals()){
                        resultListP1.add("Non terminal " + nt);
                            for(ArrayList<String> a : gramP1.First(i,nt)){ // need changes
                                sb.delete(0, sb.length());
                                for(String s : a)
                                    sb.append(s);
                                resultListP1.add(sb.toString());
                            }
                   }
               }
               catch(Exception exc){
                   JOptionPane.showMessageDialog(rootPane, exc.toString());
               }
       }
       if("followk".equals(e.getActionCommand())){
           StringBuilder sb = new StringBuilder();
           resultListP1.clear();
           resultListP1.add("FollowK table is:");
           String result = JOptionPane.showInputDialog(rootPane, "Input k");
               try{
                   int i = Integer.parseInt(result);
                   for(String nt: gramP1.GetNonTerminals()){
                      resultListP1.add("Non terminal " + nt);
                      for(ArrayList<String> a : gramP1.Follow(i,nt)){ // need changes
                          sb.delete(0, sb.length());
                          for(String s : a)
                              sb.append(s);
                          resultListP1.add(sb.toString());
                      }
                  } 
               }
               catch(Exception exc){
                   JOptionPane.showMessageDialog(rootPane, exc.toString());
               }
       }
       if("time_out".equals(e.getActionCommand())){
           program = progTextPaneP2.getText();
           int pos = progTextPaneP2.getCaretPosition();
           progTextPaneP2.setText("");
           
           Tokenizer tok = new Tokenizer(program);
           Tokenizer.Token curToken;
           int p = 0;
           do{
               curToken = tok.getNextToken();
               if(errorPos - 1 == p){
                   progTextPaneP2.append(Color.RED, curToken.value);
                   continue;
               }
               else
                   ++p;
               if(curToken.type.equals(Tokenizer.TokType.ASSIGNOP))
                   progTextPaneP2.append(Color.PINK, curToken.value);
               else
               if(curToken.type.equals(Tokenizer.TokType.DOUBLEOP))
                   progTextPaneP2.append(Color.ORANGE, curToken.value);
               else
               if(curToken.type.equals(Tokenizer.TokType.UNKNOWN))
                   progTextPaneP2.append(Color.red, curToken.value);
               else
               if(curToken.type.equals(Tokenizer.TokType.LITERAL))
                   progTextPaneP2.append(Color.CYAN, curToken.value);
               else
               if(curToken.type.equals(Tokenizer.TokType.KWORIDENT)){
                   if(keywords.indexOf(curToken.value) != -1)
                       progTextPaneP2.append(Color.BLUE, curToken.value);
                   else
                       progTextPaneP2.append(Color.GREEN, curToken.value);
               }
               else
               if(curToken.type.equals(Tokenizer.TokType.COMMENT))
                   progTextPaneP2.append(Color.gray, curToken.value);
               else
               if(curToken.type.equals(Tokenizer.TokType.NUMBER))
                   progTextPaneP2.append(Color.MAGENTA, curToken.value);
               else
               if(curToken.type.equals(Tokenizer.TokType.SPACE))
                   progTextPaneP2.append(Color.white, curToken.value);
               else
                   progTextPaneP2.append(Color.black, curToken.value);
               }while(!curToken.value.isEmpty());
           //if(pos > progTextPaneP2.getText().length())
            progTextPaneP2.setCaretPosition(pos);
       }
    }
}
