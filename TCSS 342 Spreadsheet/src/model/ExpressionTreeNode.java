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
 * This class represents an individual Expression Tree Node.
 * 
 * @author Jonah Howard
 * @version 28 Feb 2016
 */
public class ExpressionTreeNode {

	/** The token for this node. */
	protected Token token;
	
	/** References the left node to this node. */
	protected ExpressionTreeNode left;
	
	/** References the right node to this node. */
	protected ExpressionTreeNode right;
	
	/**
	 * Initializes a new ExpressionTreeNode.
	 * 
	 * @param theToken 	the token for this node
	 * @param theLeft	the left node for this node
	 * @param theRight	the right node for this node
	 */
	public ExpressionTreeNode(final Token theToken, final ExpressionTreeNode theLeft, 
			final ExpressionTreeNode theRight) {
		token = theToken;
		left = theLeft;
		right = theRight;
	}
	
	/**
	 * Initializes a new ExpressionTreeNode.
	 * 
	 * @param theToken the token for this node
	 */
	public ExpressionTreeNode(final Token theToken) {
		this(theToken, null, null);
	}
	
	@Override
	public String toString() {
		return token.toString();
	}
}
