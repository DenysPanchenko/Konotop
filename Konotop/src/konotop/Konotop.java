package konotop;

import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JTextArea;
import javax.swing.JMenuItem;
import javax.swing.DefaultListModel;
import javax.swing.JFormattedTextField;

import javax.swing.JButton;
import javax.swing.GroupLayout;

public class Konotop {
    
    public static void main(String[] args) {
        
        JMenuBar mainMenu = new JMenuBar(); //create main menu
        
        JMenu menu = new JMenu("File");
        JMenuItem item = new JMenuItem("Open");
        menu.add(item);
        item = new JMenuItem("Save");
        menu.add(item);
        item = new JMenuItem("Exit");
        menu.add(item);
        
        mainMenu.add(menu);
        
        menu = new JMenu("Algorithm");
        item = new JMenuItem("Remove useless terminals");
        menu.add(item);
        item = new JMenuItem("Derivation");
        menu.add(item);
        
        mainMenu.add(menu);
        
        DefaultListModel listModel = new DefaultListModel(); //create list model with description of grammatic
        listModel.addElement("The grammatics is:        ");
        listModel.addElement(" ");
        listModel.addElement(" ");
        listModel.addElement(" ");
        
        JList list = new JList(listModel); //create list for grammatic visualisation
        
        JFormattedTextField input = new JFormattedTextField(); //field for grammar rules input
        
        JTextArea textArea = new JTextArea(); //text area for algorithm result visualisation
        textArea.setEditable(false);
        textArea.setText("The result:        ");
        
        JButton addButton = new JButton("Add");
        JButton delButton = new JButton("Delete");
        
        JFrame mainWindow = new JFrame("Konotop project"); //create main window
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        GroupLayout layout = new GroupLayout(mainWindow.getContentPane());
        mainWindow.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(list)
                    .addComponent(input)
                ) 
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addComponent(delButton)
                    )
                    .addComponent(textArea)
                )
        );
        
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(list)
                    .addComponent(textArea)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(input)
                    .addComponent(addButton)
                    .addComponent(delButton)
                )
        );
        
        mainWindow.setJMenuBar(mainMenu);
        mainWindow.setResizable(false);
        mainWindow.pack();
        mainWindow.setVisible(true);
    }
};