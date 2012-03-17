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

import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.JOptionPane;


public class Gui extends JFrame implements ActionListener, ComponentListener, ItemListener {
    private List                gramList;
    private List                resultList;
    private JButton             addButton;
    private JButton             delButton;
    private JMenuBar            mainMenu;
    private GroupLayout         layout;
    private JFileChooser        fileChooser;
    private DefaultListModel    gramListModel;
    private JFormattedTextField input;

    private Grammar gram;
    private FAParser parser;

    public Gui() {
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
        gramList.setMultipleMode(false);
        gramList.add("Grammatic is:");
        gramList.addItemListener(this);

        input = new JFormattedTextField(); //field for grammar rules input

        resultList = new List(); //text area for algorithm result visualisation
        //resultTextArea.setEditable(false);
        //resultTextArea.setText("The result:");
        //resultTextArea.setLineWrap(true);

        addButton = new JButton("Add");
        addButton.setActionCommand("add");
        addButton.addActionListener(this);
        addButton.setEnabled(true);
        delButton = new JButton("Delete");
        delButton.setActionCommand("del");
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
        
        parser = new FAParser();
        
        //String s1 = new String("    asdfsfa final\t\t\t \n\nsjdfasdf    fdsf ds f     sfd ");
        //resultList.add(removeExtraSpaces(s1));
    }
    
    private void printGrammar(List list, Grammar gram, String title){
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
        for(i = 0; i < gramList.getItemCount(); i++)
            if(gramList.getItem(i).equals("Rules:")){
                pos = i;
                break;
            }
        //System.out.println(new Integer(pos).toString());
        
        if(gramList.getSelectedIndex() > pos)
            delButton.setEnabled(true);
        else
            delButton.setEnabled(false);
    }

    @Override
    public void componentResized(ComponentEvent e){
        if(e.getComponent().getClass().getName().equals(this.getClass().getName())){

        }
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
       if("open".equals(e.getActionCommand())){
           int returnVal = fileChooser.showOpenDialog(this);
           if(returnVal == JFileChooser.APPROVE_OPTION){
               File bnfFile = fileChooser.getSelectedFile();
               try{
                parser.setInputFile(bnfFile);
                parser.parseFile();
               }
               catch(Exception exc){
                   
               }
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
       if("del".equals(e.getActionCommand())){
           int index = gramList.getSelectedIndex();
           int j, pos = 0;
           for(j = 0; j < gramList.getItemCount(); j++){
               if("Rules:".equals(gramList.getItem(j))){
                   pos = j;
                   break;
               }
           }
           index -= (pos + 1);
           j = 0;
           for(Rule r : gram.GetRules()){
               if(j == index){
                   HashSet<Rule> nRules = gram.GetRules();
                   nRules.remove(r);
                   gram.SetRules(nRules);
                   printGrammar(gramList, gram, "Grammar is:");
                   break;
               }
               j++;
           }
       }
       if("add".equals(e.getActionCommand())){
           String str = input.getText();
           if(str.isEmpty())
               JOptionPane.showMessageDialog(this, "Input string cannot be empty!");
           else{
               
           }
       }
    }
}
