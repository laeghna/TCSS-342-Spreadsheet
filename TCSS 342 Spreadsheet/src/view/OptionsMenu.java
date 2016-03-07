/*
 * Lisa Taylor
 * Jonah Howard
 * Henry Lai
 * John Bui
 * 
 * TCSS 342 - Spring 2016
 * Assignment 6B
 */

package view;

import model.Spreadsheet;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTable;

/**
 * An Options drop-down menu.
 * 
 * @author Lisa Taylor
 * 
 * @version 4 March 2016
 */
public class OptionsMenu implements PropertyChangeListener {
    
    /** JMenu to hold Options menu items. */
    private final JMenu myOptionsMenu;
    
    /** The JFrame that the spreadsheet is displayed on. */
    private final JFrame myFrame;
       
    /** The Spreadsheet. */
    private final Spreadsheet mySpreadsheet;
    
    /** The table that holds the cells for the spreadsheet. */
    private final JTable myTable;
    
    /** MenuItem to clear the entire spreadsheet. */
    private final JMenuItem clearAll;
    
    /** MenuItem to resize spreadsheet. */
    private final JMenuItem resize;
    
    /** MenuItem to add row(s) to spreadsheet. */
    private final JMenuItem addRows;
    
    /** MenuItem to add column(s) to spreadsheet. */
    private final JMenuItem addColumns;
    
    /** 
     * Constructor to initialize fields. 
     * 
     * @param thePanel the CanvasPanel
     */
    public OptionsMenu(final JFrame theFrame, final Spreadsheet theSpreadsheet, final JTable theTable) {
        
        myOptionsMenu = new JMenu("Options");
        
        myFrame = theFrame;
        mySpreadsheet = theSpreadsheet;
        myTable = theTable;
        
        clearAll = new JMenuItem("Clear All");
        resize = new JMenuItem("Resize");
        addRows = new JMenuItem("Add Row(s)");
        addColumns = new JMenuItem("Add Column(s)");
        
        setupOptionsMenu();
    }
    
    /**
     * Method to get myOptionsMenu field.
     * 
     * @return the Options menu
     */
    public JMenu getOptionsMenu() {
        
        return myOptionsMenu;
    }
    
    /**
     * Adds ActionListener to clearAll menu item.
     */
    private void buildClearAll() {
        
        clearAll.setEnabled(true);
        clearAll.setMnemonic(KeyEvent.VK_C);
        clearAll.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                
                mySpreadsheet.initializeSpreadsheet();
                myFrame.repaint();
            }
        });
    }
    
    /**
     * Adds ActionListener to resize menu item.
     */
    private void buildResize() {
        
        resize.setEnabled(true);
        resize.setMnemonic(KeyEvent.VK_S);
        resize.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                
                //Code here
            }
        });
    }
    
    /**
     * Adds ActionListener to addRows menu item.
     */
    private void buildAddRows() {
        
        addRows.setEnabled(true);
        addRows.setMnemonic(KeyEvent.VK_R);
        addRows.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                
                //Code here
            }
        });
    }
    
    /**
     * Adds ActionListener to addColumns menu item.
     */
    private void buildAddColumns() {
        
        addColumns.setEnabled(true);
        addColumns.setMnemonic(KeyEvent.VK_C);
        addColumns.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                
                //Code here
            }
        });
    }
    
    /** Add menu items to OptionsMenu and sets a Mnemonic. */
    private void setupOptionsMenu() {
        
        buildClearAll();
        buildResize();
        buildAddRows();
        buildAddColumns();
        
        myOptionsMenu.add(clearAll);
        myOptionsMenu.addSeparator();
        myOptionsMenu.add(resize);
        myOptionsMenu.add(addRows);
        myOptionsMenu.add(addColumns);
    }
    
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        
        if ("TableState".equals(theEvent.getPropertyName())) {
            
            clearAll.setEnabled((boolean) theEvent.getNewValue());
        }
    }
}
