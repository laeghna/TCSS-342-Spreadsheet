/*
 * Lisa Taylor
 * Jonah Howard
 * Henry Lai
 * John Bui
 * 
 * TCSS 342 - Spring 2016
 * Assignment 6B
 */

package util;

import model.OperatorToken;
import model.Token;

/**
 * Utility class for supported operators.
 * Operators and their logic are here.
 * 
 * @author Lisa Taylor
 * @author Jonah Howard
 * 
 * @version 4 March 2016
 */
public class Operators {
    
    /** Represents an addition operator. */
    public static final char PLUS = '+';

    /** Represents a subtraction operator. */
    public static final char MINUS = '-';

    /** Represents a multiplication operator. */
    public static final char MULT = '*';

    /** Represents a division operator. */
    public static final char DIV = '/';

    /** Represents a parentheses operator. */
    public static final char LT_PAREN = '(';
    
    /** Represents a right parentheses operator. */
    public static final char RT_PAREN = ')';

    /** Represents a power operator. */
    public static final char POW = '^';
    
    /** Array to hold valid operators. */
    public static final int[] ops = {PLUS, MINUS, MULT, DIV, LT_PAREN, RT_PAREN, POW};
    
    static {
        
    }
    
    /**
     * Evaluates the two passed values based on the current operator.
     * 
     * @param operand1 the first value to be considered
     * @param operand2 the second value to be considered
     * @return the result based on the current operator
     */
    public static int evaluate(final Token theToken, final int operand1, final int operand2) {
        int result = 0;
        switch (((OperatorToken) theToken).getOperatorToken()) {
            case PLUS: {
                result = operand1 + operand2;
                break;
            } case MINUS: {
                result = operand1 - operand2;
                break;
            } case MULT: {
                result = operand1 * operand2;
                break;
            } case DIV: {
                result = operand1 / operand2;
                break;
            } case POW: {
                result = (int) Math.pow(operand1, operand2);
                break;
            }
        }
        return result;
    }
    
    /**
     * Given an operator, return its priority.
     *
     * priorities:
     *   +, - : 0
     *   *, / : 1
     *   (    : 2
     *
     * @param ch  a char
     * @return  the priority of the operator
     */
    public static int operatorPriority (char ch) {
        if (!isValidOperator(ch)) {
            // This case should NEVER happen
            System.out.println("Error in operatorPriority.");
            System.exit(0);
        }
        switch (ch) {
            case PLUS:
                return 0;
            case MINUS:
                return 0;
            case MULT:
                return 1;
            case DIV:
                return 1;
            case POW:
                return 2;
            case LT_PAREN:
                return 3;
            //case RT_PAREN:
                //return 3;

            default:
                // This case should NEVER happen
                System.out.println("Error in operatorPriority.");
                System.exit(0);
                break;
        }
        return -1;
    }
    
    /**
     * Return the priority of this OperatorToken.
     *
     * priorities:
     *   +, - : 0
     *   *, / : 1
     *   ^    : 2
     *   (    : 3
     *
     * @return  the priority of operatorToken
     */
    public static int operatorPriority (final Token theToken) {
        switch (((OperatorToken) theToken).getOperatorToken()) {
            case PLUS:
                return 0;
            case MINUS:
                return 0;
            case MULT:
                return 1;
            case DIV:
                return 1;
            case POW:
                return 2;
            case LT_PAREN:
                return 3;

            default:
                // This case should NEVER happen
                System.out.println("Error in priority.");
                System.exit(0);
                break;
        }
        return -1;
    }
    
    /**
     * Return true if the char ch is an operator of a formula.
     * Current operators are: +, -, *, /, (, ^.
     * 
     * @param ch  a char
     * @return  whether ch is an operator
     */
    public static boolean isValidOperator (char ch) {
        return ((ch == Operators.PLUS) ||
                (ch == Operators.MINUS) ||
                (ch == Operators.MULT) ||
                (ch == Operators.DIV) ||
                (ch == Operators.LT_PAREN) ||
                //(ch == Operators.RT_PAREN) ||
                (ch == Operators.POW));
    }
}
