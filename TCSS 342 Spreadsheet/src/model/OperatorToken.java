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
 * This class represents an individual Operator Token. 
 * 
 * @author Jonah Howard
 * @author Lisa Taylor
 * @version 3 March 2016
 */
public class OperatorToken extends Token{

    /**
     * Initialize a new Operator Token for char passed as argument.
     * 
     * @param theOperator the current operator
     */
    public OperatorToken(final char theOperator) { 
        this(theOperator + "");
    }
    
    /**
    * Initialize a new Operator Token for string passed as argument.
    * 
    * @param theOperator the current operator
    */
    public OperatorToken(final String theOperator) {
        if (theOperator != null)
            setToken(theOperator);
    }
    
    /**
     * Return this operator token.
     * 
     * @return this operator token
     */
    public char getOperatorToken() {
        return getToken().charAt(0);
    }
}
