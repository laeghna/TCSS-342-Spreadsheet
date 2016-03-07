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

/**
 * This class represents a Cell Token.
 * 
 * @author Jonah Howard
 * @author Lisa Taylor
 * 
 * @version 3 March 2016
 */
public class CellToken extends Token {

	/** Represents a bad cell. */
	public static final int BAD_CELL = -1;
	
	/** The column for this cell token. */
	private int column;

	/** The row for this cell token. */
	private int row;

	/**
	 * Initialize a new Cell Token.
	 * 
	 * @param theFormula the formula for this cell token
	 * @param theIndex the starting index to translate the formula
	 */
	public CellToken(final String theFormula, final int theIndex) {
		getCellToken(theFormula, theIndex);
	}
	
	/**
	 * Returns the column for this cell token.
	 * 
	 * @return the column for this cell token
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Returns the row for this cell token.
	 * 
	 * @return the row for this cell token
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Returns a number referencing a location on the spreadsheet.
	 * 
	 * @param inputString the input being considered
	 * @param startIndex the starting index for the input
	 * @return a number referencing a location on the spreadsheet
	 */
	public int getCellToken(String inputString, int startIndex) {
		char ch;
		int column = 0;
		int row = 0;
		int index = startIndex;

		// handle a bad startIndex
		if ((startIndex < 0) || (startIndex >= inputString.length())) {
			setColumn(BAD_CELL);
			setRow(BAD_CELL);
			return index;
		}

		// get rid of leading whitespace characters
		while (index < inputString.length()) {
			ch = inputString.charAt(index);
			if (!Character.isWhitespace(ch)) {
				break;
			}
			index++;
		}
		if (index == inputString.length()) {
			// reached the end of the string before finding a capital letter
			setColumn(BAD_CELL);
			setRow(BAD_CELL);
			return index;
		}

		// ASSERT: index now points to the first non-whitespace character

		ch = inputString.charAt(index);
		// process CAPITAL alphabetic characters to calculate the column
		if (Character.isLowerCase(ch)) {
		    Character.toUpperCase(ch);
		}
        column = ch - 'A' + 1;
		index++;
		

		while (index < inputString.length()) {
			ch = inputString.charAt(index);
			if (Character.isUpperCase(ch)) {
				column = (column * 26) + (ch - 'A') + 1;
				index++;
			} else {
				break;
			}
		}
		if (index == inputString.length()) {
			// reached the end of the string before fully parsing the cell
			// reference
			setColumn(BAD_CELL);
			setRow(BAD_CELL);
			return index;
		}

		// ASSERT: We have processed leading whitespace and the
		// capital letters of the cell reference

		// read numeric characters to calculate the row
		if (Character.isDigit(ch)) {
			row = ch - '0';
			index++;
		} else {
			setColumn(BAD_CELL);
			setRow(BAD_CELL);
			return index;
		}

		while (index < inputString.length()) {
			ch = inputString.charAt(index);
			if (Character.isDigit(ch)) {
				row = (row * 10) + (ch - '0');
				index++;
			} else {
				break;
			}
		}

		// successfully parsed a cell reference
		setColumn(column);
		setRow(row - 1);
		return index;
	}
	
	/**
	 * Set the column of this cell token.
	 * 
	 * @param theColumn the column this cell token is to be set to
	 */
	public void setColumn(final int theColumn) {
		column = theColumn;
	}
	
	/**
	 * Set the row of this cell token.
	 * 
	 * @param theRow the row this cell token is to be set to
	 */
	public void setRow(final int theRow) {
		row = theRow;
	}
	
	@Override
	public String toString() {
		return Spreadsheet.convertToString(column) + Integer.toString(row + 1);
	}
}
