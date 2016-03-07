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

import javax.swing.table.AbstractTableModel;

import model.Cell;

/**
 * This class represents the table for the spreadsheet.
 * 
 * @author Lisa Taylor
 * 
 * @version 6 March 2016
 */
public class Table extends AbstractTableModel {

    /** The row numbers for the spreadsheet. */
    private final int myRows;
    
    /** The column names for the spreadsheet. */
    private final int myColumns;
    
    /** The cells for the spreadsheet. */
    private final Cell[][] myCells;
    
    public Table(final int rows, final int columns, final Cell[][]cells) {
        
        myRows = rows + 1;
        myColumns = columns + 1;
        myCells = cells;
        
    }

    @Override
    /** Returns the number of rows in the table. */
    public int getRowCount() {

        return myRows;
    }
    
    @Override
    /** Returns the number of columns in the table. */
    public int getColumnCount() {

        return myColumns;
    }

    @Override
    public Object getValueAt(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    
}
