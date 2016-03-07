/*
 * Lisa Taylor
 * Jonah Howard
 * Henry Lai
 * John Bui
 * 
 * TCSS 342 - Spring 2016
 * Assignment 6B
 */

package model;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * This class represents a spreadsheet. It stores the information for all of the
 * cells of the spreadsheet.
 * 
 * @author Jonah Howard
 * @author Lisa Taylor
 * 
 * @vesion 3 March 2016
 */
public class Spreadsheet extends DefaultTableModel implements TableModelListener {

	/** A generated Serial Version UID. */
	private static final long serialVersionUID = 9025127485326978066L;
	
	/** The width of the column of row numbers in pixels. */
	public static final int ROW_NUMBER_WIDTH = 30;
	
	/** Minimum size for the spreadsheet. */
	public static final int MIN_SPREADSHEET_SIZE = 3;
	
	/** Count of letters. */
	public static final int NUM_LETTERS = 26;

	/** How many columns are in this spreadsheet. */
	private int myColumns;
	
	/** How many rows are in this spreadsheet. */
	private int myRows;

	/** The current spreadsheet. */
	private Cell[][] mySpreadsheet;

	/** Represents the names of the columns. */
	private final Object[] columnNames;

	/** Represents the current JTable. */
	private final JTable myTable;

	/** True if the Display Formulas button is pressed, false otherwise. */
	private boolean displayFormulas;

	/**
	 * Initializes a new Spreadsheet.
	 * 
	 * @param theWidth the width of this spreadsheet
	 * @param theHeight the height of this spreadsheet
	 */
	public Spreadsheet(final int theWidth, final int theHeight) {
	    
	    displayFormulas = true;
		myColumns = theWidth;
		myRows = theHeight;
		columnNames = new String[myColumns + 1];

        fillColumnNames();
        initializeSpreadsheet();
        
		myTable = new JTable(mySpreadsheet, columnNames) {
			/** A generated serial version UID. */
			private static final long serialVersionUID = -8427343693180623327L;

			//Disables the row numbers from being editable.
			@Override
			public boolean isCellEditable(int row, int column) {
				return column != 0;
			}
		};
		
        setupAllCells();
	}

	@Override
	public void tableChanged(final TableModelEvent theEvent) {
		// Save contents of cell before changing.
		Cell theCell = mySpreadsheet[theEvent.getFirstRow()][theEvent.getColumn()];
        String formula = mySpreadsheet[theEvent.getFirstRow()][theEvent.getColumn()].getFormula();
        String oldFormula = theCell.getFormula();
        int oldValue = theCell.getValue();
        try {
            // Tries to parse the expression entered by the user.
    	    theCell.parseInput(formula);
        } catch (NullPointerException e) {
	        theCell.setHasInput(false);
	        theCell.removeAllDependencies();
	        theCell.setFormula(null);
	        theCell.setValue(0);
	        theCell.updateDependents();
	    } catch (CircularDependencyException e) {
	        JOptionPane.showMessageDialog(myTable.getParent(), "Circular Dependency found. Reverting back to"
	                                                         + " previous entry.", "Error!", JOptionPane.ERROR_MESSAGE);
	        // Revert to old formula in spreadsheet and cells
	        if (displayFormulas) {
	            // Display reverted formula if in formula mode
                mySpreadsheet[theEvent.getFirstRow()][theEvent.getColumn()].setFormula(oldFormula);
	        } else {
	            // Display reverted formula if in value mode
	            mySpreadsheet[theEvent.getFirstRow()][theEvent.getColumn()].setValue(oldValue);
	        }
            theCell.setFormula(oldFormula);
            theCell.setHasCircDepend(false);
	    } catch (ArithmeticException e) {
		    JOptionPane.showMessageDialog(myTable.getParent(), "HAHAHA NICE TRY. Please try again.", "Error!",
			    	JOptionPane.ERROR_MESSAGE);
		    // Revert to old formula in spreadsheet and cells
            if (displayFormulas) {
                // Display reverted formula if in formula mode
                mySpreadsheet[theEvent.getFirstRow()][theEvent.getColumn()].setFormula(oldFormula);
            } else {
                // Display reverted formula if in value mode
                mySpreadsheet[theEvent.getFirstRow()][theEvent.getColumn()].setValue(oldValue);
            }
		    theCell.setFormula(oldFormula);
	    } catch (Exception e){
    	    // Display an error and revert to old formula if invalid input.
		    JOptionPane.showMessageDialog(myTable.getParent(), "Invalid expression entered. Please try again.", "Error!",
			    	JOptionPane.ERROR_MESSAGE);
		    // Revert to old formula in cells if displaying formula.
		    theCell.setFormula(oldFormula);
		    if (displayFormulas) {
			    // Display reverted formula if in formula mode.
			    mySpreadsheet[theEvent.getFirstRow()][theEvent.getColumn()].setFormula(oldFormula);
		    } else {
			    // Display reverted value if in value mode.
   				mySpreadsheet[theEvent.getFirstRow()][theEvent.getColumn()].setValue(oldValue);
    		}
	    }
	    System.out.println(mySpreadsheet[theEvent.getFirstRow()][theEvent.getColumn()].toString());
//		printAllFormulas();	
    }

	/**
	 * Returns the current JTable.
	 * 
	 * @return the current table
	 */
	public JTable getTable() {
		return myTable;
	}

	/**
	 * updates the spreadsheet at the location with respect to the passed row
	 * and column.
	 * 
	 * @param theRow the row of the spreadsheet to be updated
	 * @param theColumn the column of the spreadsheet to be updated
	 */
	public void updateSpreadsheet(final int theRow, final int theColumn) {
		// Check whether the display formulas button is pressed or display values
		if (displayFormulas) {
			mySpreadsheet[theRow][theColumn].getFormula();
		} else {
			if (mySpreadsheet[theRow][theColumn].hasInput()) {
				mySpreadsheet[theRow][theColumn].getValue();
			} else {
				mySpreadsheet[theRow][theColumn].setFormula("");
			}
		}
	}

	/**
	 * Fill the columns with their respective letter representations..
	 */
	private void fillColumnNames() {
		columnNames[0] = "";
		for (int col = 1; col < myColumns + 1; col++) {
			columnNames[col] = convertToString(col - 1);
		}
	}

	/**
	 * Initialize the spreadsheet array.
	 * 
	 * @return the spreadsheet array
	 */
	public void initializeSpreadsheet() {
		mySpreadsheet = new Cell[myRows][myColumns + 1];
		for (int row = 0; row < myRows; row++) {
			for (int col = 0; col < myColumns + 1; col++) {
			    mySpreadsheet[row][col] = new Cell(row, col, this);
				// First column is set to a number in ascending order, all others are empty
				if (col == 0) {
					mySpreadsheet[row][col].setValue(row + 1);
					//mySpreadsheet[row][col].setFormula(Integer.toString(row + 1));
				} else {
					mySpreadsheet[row][col].setFormula("");
				}
			}
		}
	}

	/**
	 * Centers all the cells be setting each columns default cell
	 * render to center the cell's data. It also colors the background of the
	 * row numbers to indicate that they are part of the UI and is in-editable.
	 */
	private void setupAllCells() {
		// For all data columns in the table, center their cell's alignment. 
		for (int col = 1; col < myColumns + 1; col++) {
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			myTable.getColumnModel().getColumn(col).setCellRenderer(centerRenderer);
		}
		
		// Special case for first column, set the background color to match the column headers
		// in addition to centering each cell. 
		TableColumn rowNums = myTable.getColumnModel().getColumn(0);
		rowNums.setCellRenderer(new DefaultTableCellRenderer() {
			
			/** A generated serial version UID. */
			private static final long serialVersionUID = 3565976393614019090L;
			// Comment
			@Override
			public Component getTableCellRendererComponent(final JTable table, 
					final Object value, final boolean isSelected, final boolean hasFocus, 
					final int row, final int column) {
				final Component cell = super.getTableCellRendererComponent(table, value, 
						isSelected, hasFocus, row, column);
				final Color lightGray = new Color(238, 238, 238);
				cell.setBackground(lightGray);
				setHorizontalAlignment(JLabel.CENTER);
				return cell;
			}
		});
		
		// Set interface properties for the table
		myTable.getColumnModel().getColumn(0).setPreferredWidth(ROW_NUMBER_WIDTH);
		myTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		myTable.getModel().addTableModelListener(this);
		myTable.getTableHeader().setReorderingAllowed(false);
	}

	/**
	 * Return the column names.
	 * 
	 * @return the column names
	 */
	public Object[] getColumnNames() {
		return columnNames;
	}

	/**
	 * Return the spreadsheet.
	 * 
	 * @return the spreadsheet
	 */
	public Cell[][] getSpreadsheet() {
		return mySpreadsheet;
	}

	/**
	 * Returns the String representation of the passed column.
	 * 
	 * @param theColumn the current column being converted
	 * @return the String representation of the passed column
	 */
	public static String convertToString(int theColumn) {
		StringBuilder result = new StringBuilder();
		do {
			// Solve rounding issue
			if (result.length() > 0) {
				theColumn--;
			}
			result.insert(0, (char) ((theColumn % NUM_LETTERS) + 65));
			theColumn /= NUM_LETTERS;
		} while (theColumn > 0);
		return result.toString();
	}

	/**
	 * Returns the integer version of the passed column.
	 * 
	 * @param theColumn the current column being converted
	 * @return the integer version of the passed column
	 */
	public static int convertToInt(final String theColumn) {
		int current = 0;
		int result = 0;
		int currentLetter;
		while (current < theColumn.length()) {
			if (current > 0) {
				result++;
			}
			currentLetter = theColumn.charAt(current) - 65;
			result *= NUM_LETTERS;
			result += currentLetter;
			current++;
		}
		return result;
	}

	/**
	 * Prints the formula for the passed cell token.
	 * 
	 * @param theToken The cell whose formula is being printed.
	 */
	public void printCellFormula(final CellToken theToken) {
		Cell theCell = mySpreadsheet[theToken.getRow()][theToken.getColumn()];
		System.out.println(theCell.getFormula());
	}

	/**
	 * Prints the formulas of all cells.
	 */
	public void printAllFormulas() {
		for (int row = 0; row < myRows; row++) {
			for (int col = 1; col < myColumns; col++) {
			    
				// Prints the Column and Row with colon (e.g. A4: )
				System.out.print(convertToString(col - 1) + row + ": ");
				
				// Prints the formula for that cell
				System.out.print(mySpreadsheet[row][col].getFormula() + "   ");
			}
			// Line break at the end of a row.
			System.out.println();
		}
	}

	/**
	 * Returns the number of rows for this spreadsheet.
	 * 
	 * @return the number of rows for this spreadsheet
	 */
	public int getRows() {
		return myRows;
	}
	
	/**
	 * Returns the number of columns for this spreadsheet.
	 * 
	 * @return the number of columns for this spreadsheet
	 */
	public int getColumns() {
		return myColumns;
	}

    /**
     * Notifies the spreadsheet when one of the buttons has been pressed.
     * 
     * @param bool true if the display formulas button is pressed, false otherwise
     */
    public void setDisplayFormulas(final boolean bool) {
        displayFormulas = bool;
    }
    
	/**
     * Returns whether in Display Formulas mode.
     * 
     * @return true if viewing formula mode, else false
     */
    public boolean getDisplayFormulas() {
        return displayFormulas;
    }
}