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
 * Exception class for circular dependency.
 *
 * @author Lisa Taylor
 * @author Jonah Howard
 * 
 * @version 3 March 2016
 */
public class CircularDependencyException extends Exception {
	
    /** A generated serial version UID. */
	private static final long serialVersionUID = 1754962508847115076L;

	/**
	 * Initialize a new CircularDependencyException.
	 */
	public CircularDependencyException() {
    }

	/**
	 * Initializes a new CircularDependencyException.
	 * 
	 * @param message the message to be displayed when this exception occurs.
	 */
    public CircularDependencyException(final String message) {
        super(message);
    }
}
