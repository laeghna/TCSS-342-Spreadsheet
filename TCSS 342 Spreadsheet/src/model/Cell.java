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

import util.Operators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

/**
 * This class represents a single cell in a spreadsheet. It contains data
 * specific to this cell and can depend on the myValue of other cells for
 * mathematical computations.
 * 
 * @author Jonah Howard
 * @author Lisa Taylor
 * @author Henry Lai
 * @author John Bui
 * 
 * @version 3 March 2016
 */
public class Cell {

	/** Represents the myFormula corresponding to this cell. */
	private String myFormula;

	/** The myValue contained in this cell. */
	private int myValue;

	/** The expression tree for this cell. */
	private final ExpressionTree expressionTree;

	/** The list of dependencies for this cell. */
	private final List<Cell> myDependencies;

	private final List<Cell> myDependents;

	/** The current row for this cell. */
	private final int myRow;

	/** The current column for this cell. */
	private final int myColumn;

	/** Maps an indegree to a list of cells that are dependent on this cell. */
	private final Map<Integer, List<Cell>> dependents;
	
	/** The current spreadsheet. */
	private final Spreadsheet mySpreadsheet;
	
	/** True if circular dependency is found, otherwise false. */
	private boolean hasCircDepend;
	
	/** True if cell contains input from user, otherwise false. */
	private boolean hasInput;

	/**
	 * Initializes a new cell.
	 * 
	 * @param theColumn The column where this cell is located.
	 * @param theRow The row where this cell is located.
	 */
	public Cell(final int theRow, final int theColumn, final Spreadsheet theSpreadsheet) {
		mySpreadsheet = theSpreadsheet;
		hasCircDepend = false;
		hasInput = false;
		expressionTree = new ExpressionTree(theSpreadsheet);
		myValue = 0;
		myDependencies = new ArrayList<Cell>();
		myDependents = new ArrayList<Cell>();
		myColumn = theColumn;
		myRow = theRow;
		dependents = new TreeMap<Integer, List<Cell>>();
	}

	/**
	 * Add the passed token to the list of dependents.
	 * 
	 * @param theToken
	 *            the token that depends on this cell
	 */
	public void addDependent(final Cell theCell) {
		myDependents.add(theCell);
	}

	/**
	 * Removes a dependent from the list of dependents.
	 * 
	 * @param theCell the dependent to be removed
	 * @param inDegree the inDegree of the passed cell
	 */
	public void removeDependent(final int inDegree, final Cell theCell) {
		dependents.get(inDegree).remove(theCell);
		for (Iterator<Cell> iterator = myDependents.iterator(); iterator.hasNext();) {
			Cell cell = iterator.next();
			if (cell == theCell) {
				iterator.remove();
			}
		}
	}

	/**
	 * Adds a dependent to the map of indegrees to cells.
	 * 
	 * @param inDegree
	 *            the indegree of the passed cell
	 * @param theCell
	 *            the cell to be added
	 */
	public void addDependent(final int inDegree, final Cell theCell) {
		if (dependents.containsKey(inDegree)) {
			dependents.get(inDegree).add(theCell);
		} else {
			dependents.put(inDegree, new ArrayList<Cell>());
			dependents.get(inDegree).add(theCell);
		}
	}

	/**
	 * Returns the number of dependencies for this cell.
	 * 
	 * @return the number of dependencies for this cell
	 */
	public int getDependencyCount() {
		return myDependencies.size();
	}

	/**
	 * Notifies each cell in the list of dependents that a change was made to
	 * this cell. Evaluates in topological sorting order.
	 */
	public void updateDependents() {
		for (final int current : dependents.keySet()) {
			for (Cell cell : dependents.get(current)) {
				mySpreadsheet.getSpreadsheet()[cell.myRow][cell.myColumn].reEvaluate();

			}
		}
	}

	/**
	 * Parse and evaluate input for this cell. Notifies any dependents that this cell has been
	 * changed.
	 * 
	 * @param input The new input for this cell
	 * @throws CircularDependencyException 
	 */
	public void parseInput(final String input) throws CircularDependencyException {
	    final Stack<Token> formula = getFormula(input);
	    checkForCircularDependency(this);
	    if (hasCircDepend)
	        throw new CircularDependencyException();
	    else {
	        setHasInput(true);
	        expressionTree.BuildExpressionTree(formula);
	        myFormula = input;
	        myValue = expressionTree.evaluate();
	        if (!myDependents.isEmpty()) {
		        updateDependents();
	        }
	        mySpreadsheet.updateSpreadsheet(myRow, myColumn);
	    }
	    
	}

	/**
	 * Evaluates this cell if a change is made to one of its dependencies.
	 */
	public void reEvaluate() {
	    if (!hasCircDepend) {
		    myValue = expressionTree.evaluate();
		    updateDependents();
		    mySpreadsheet.updateSpreadsheet(myRow, myColumn);
	    }
	}

	/**
	 * Return the myValue for this cell.
	 * 
	 * @return the myValue for this cell
	 */
	public int getValue() {
		return myValue;
	}
	
	/**
	 * Set the myValue for this cell.
	 * 
	 * @param value the value to be set
	 */
	public void setValue(final int value) {
	    myValue = value;
	}

	/**
	 * Removes all dependencies from this cell.
	 */
	public void removeAllDependencies() {
		for (final Cell cell : myDependencies) {
			cell.removeDependent(myDependencies.size(), this);
		}
		myDependencies.clear();
	}

	/**
	 * Returns a stack representing the passed myFormula.
	 * 
	 * @param myFormula the current myFormula being considered
	 * @return A stack representing the passed myFormula
	 */
	public Stack<Token> getFormula(String myFormula) throws CircularDependencyException {
		removeAllDependencies();
		Stack<Token> returnStack = new Stack<Token>(); // stack of Tokens
														// (representing a
														// postfix expression)
		boolean error = false;
		char ch = ' ';

		int literalValue = 0;

		CellToken cellToken;

		int index = 0; // index into myFormula
		Stack<Token> operatorStack = new Stack<Token>(); // stack of operators

		while (index < myFormula.length()) {
		    
			// get rid of leading whitespace characters
			while (index < myFormula.length()) {
				ch = myFormula.charAt(index);
				if (!Character.isWhitespace(ch)) {
					break;
				}
				index++;
			}

			if (index == myFormula.length()) {
				error = true;
				break;
			}

			// ASSERT: ch now contains the first character of the next token.
			if (Operators.isValidOperator(ch)) {
			    try {
					// push operatorTokens onto the output stack until
					// we reach an operator on the operator stack that has
					// lower priority than the current one.
					OperatorToken stackOperator;
					while (!operatorStack.isEmpty()) {
						stackOperator = (OperatorToken) operatorStack.peek();
						if ((Operators.operatorPriority(stackOperator) >= Operators.operatorPriority(ch))
								&& (stackOperator.getOperatorToken() != Operators.LT_PAREN)) {

							// output the operator to the return stack
							operatorStack.pop();
							returnStack.push(stackOperator);
						} else {
							break;
						}
					}
					if (ch == Operators.LT_PAREN && myFormula.charAt(index + 1) == Operators.MINUS) {
						returnStack.push(new LiteralToken(0));
					}
			    } catch (Exception e){
					// This case should NEVER happen
					System.out.println("Error in getFormula.");
					System.exit(0);
				}
				// Check if there was a negative value entered
				if (returnStack.size() == 0 && ch == Operators.MINUS) {
					returnStack.push(new LiteralToken(0));
				}
				// push the operator on the operator stack
				operatorStack.push(new OperatorToken(ch));

				index++;

			} else if (ch == ')') { // maybe define OperatorToken.RightParen ?
				OperatorToken stackOperator;
				stackOperator = (OperatorToken) operatorStack.pop();
				// This code does not handle operatorStack underflow.
				while (stackOperator.getOperatorToken() != Operators.LT_PAREN) {
					// pop operators off the stack until a LeftParen appears and
					// place the operators on the output stack
					returnStack.push(stackOperator);
					stackOperator = (OperatorToken) operatorStack.pop();
				}

				index++;
			} else if (Character.isDigit(ch)) {
				// We found a literal token
				literalValue = ch - '0';
				index++;
				while (index < myFormula.length()) {
					ch = myFormula.charAt(index);
					if (Character.isDigit(ch)) {
						literalValue = (literalValue * 10) + (ch - '0');
						index++;
					} else {
						break;
					}
				}
				// place the literal on the output stack
				returnStack.push(new LiteralToken(literalValue));

			} else if (Character.isUpperCase(ch)) {
				// We found a cell reference token
				cellToken = new CellToken(myFormula, index);
				mySpreadsheet.getSpreadsheet()[cellToken.getRow()][cellToken.getColumn()]
						.addDependent(this);
				myDependencies
						.add(mySpreadsheet.getSpreadsheet()[cellToken.getRow()][cellToken
								.getColumn()]);
				index = cellToken.getCellToken(myFormula, index);
				if (cellToken.getRow() == CellToken.BAD_CELL) {
					error = true;
					break;
				} else {
					// place the cell reference on the output stack
					returnStack.push(cellToken);
				}

			} else {
				error = true;
				break;
			}
		}

		// pop all remaining operators off the operator stack
		while (!operatorStack.isEmpty()) {
			returnStack.push(operatorStack.pop());
		}

		if (error) {
			// a parse error; return the empty stack
			returnStack.empty();
		}
		// Add reference to this cell and its in-degree to each dependency
		for (final Cell cell : myDependencies) {
			cell.addDependent(myDependencies.size(), this);
		}
		return returnStack;
	}

	/**
	 * Returns the myFormula for this cell.
	 * 
	 * @return Returns a string that represents this cell's myFormula.
	 */
	public String getFormula() {
	    
		return myFormula;
	}
	
	/**
	 * Sets the formula for the cell.
	 * 
	 * @param theFormula The formula for this cell.
	 */
	public void setFormula(final String theFormula) {
	    
		myFormula = theFormula;
	}
	
	/**
	 * Returns whether cell has circular dependency.
	 * 
	 * @return true if has circular dependency, else false
	 */
	public boolean hasCircularDependency() {
	    
	    return hasCircDepend;
	}
	
	/**
	 * Sets the boolean value for circDepend.
	 * 
	 * @param bool the boolean value
	 */
	public void setHasCircDepend(boolean bool) {
	    
	    hasCircDepend = bool;
	}
	
	/**
	 * Checks if circular dependency exists in spreadsheet. Cell first compares itself
	 * to the cells that it depends on and then recursively compares itself to the cells 
	 * that its dependencies depend on.
	 * 
	 * @param cell the original cell
	 */
	public void checkForCircularDependency(Cell cell) {
	    
	    for (Cell in : myDependencies){
	        
	        if (in.equals(cell)) {
	            
	            cell.setHasCircDepend(true);
	            break;
	        }
	        
	        in.checkForCircularDependency(cell);
	        for (Cell out : myDependents) {
	            
	            if (in.equals(out)) {
	                
	                setHasCircDepend(true);
	                break;
	            }
	        }
	    }
	}
	
	/** 
	 * Returns whether cell contains input.
	 * 
	 * @return true if contains input, else false
	 */
	public boolean hasInput() {
	    
	    return hasInput;
	}
	
	/**
	 * Sets the boolean value for hasInput.
	 * 
	 * @param bool the boolean value
	 */
	public void setHasInput(final boolean bool) {
	    
	    hasInput = bool;
	}

	/**
	 * Prints out a representation of this cell's internal status.
	 * 
	 * @return A string representation of the cell's internal status.
	 */
	public String toString() {
	    
	    String str = "";
	    
	    if (mySpreadsheet.getDisplayFormulas() && myColumn > 0)
	        
	        str = myFormula;
	    
	    else if (myColumn == 0 || hasInput)
	        
		    str = Integer.toString(myValue);
	    
	    return str;
	}
}
